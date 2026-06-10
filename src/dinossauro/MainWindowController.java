package dinossauro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TabPane;
import model.dao.DinossauroDAO;
import model.dto.DinossauroDTO;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindowController {

    private static final Logger LOGGER = Logger.getLogger(MainWindowController.class.getName());

    @FXML private TabPane tabPane;

    // List tab
    @FXML private TableView<DinossauroDTO> tabelaDinossauros;
    @FXML private TableColumn<DinossauroDTO, Integer> colId;
    @FXML private TableColumn<DinossauroDTO, String> colNome;
    @FXML private TableColumn<DinossauroDTO, String> colEspecie;
    @FXML private TableColumn<DinossauroDTO, Integer> colPeso;
    @FXML private TableColumn<DinossauroDTO, Double> colAltura;
    @FXML private TableColumn<DinossauroDTO, Double> colComprimento;
    @FXML private TableColumn<DinossauroDTO, String> colComportamento;
    @FXML private TableColumn<DinossauroDTO, Object> colDataCriacao;

    // Cadastro tab
    @FXML private TextField txtNomeCadastro;
    @FXML private TextField txtEspecieCadastro;
    @FXML private TextField txtPesoCadastro;
    @FXML private TextField txtAlturaCadastro;
    @FXML private TextField txtComprimentoCadastro;
    @FXML private TextField txtComportamentoCadastro;

    // Edicao tab
    @FXML private TextField txtIdEdicao;
    @FXML private TextField txtNomeEdicao;
    @FXML private TextField txtEspecieEdicao;
    @FXML private TextField txtPesoEdicao;
    @FXML private TextField txtAlturaEdicao;
    @FXML private TextField txtComprimentoEdicao;
    @FXML private TextField txtComportamentoEdicao;

    // Exclusao tab
    @FXML private TableView<DinossauroDTO> tabelaExclusao;
    @FXML private TextField txtIdExclusao;

    @FXML private Button btnAtualizarLista;

    private final DinossauroDAO dao = new DinossauroDAO();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        colComprimento.setCellValueFactory(new PropertyValueFactory<>("comprimento"));
        colComportamento.setCellValueFactory(new PropertyValueFactory<>("comportamento"));
        colDataCriacao.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));

        carregarLista();
    }

    private void carregarLista() {
        try {
            List<DinossauroDTO> lista = dao.listar();
            ObservableList<DinossauroDTO> obs = FXCollections.observableArrayList(lista);
            tabelaDinossauros.setItems(obs);
            tabelaExclusao.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar lista de dinossauros", e);
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar a lista.", e.getMessage());
        }
    }

    @FXML
    private void handleAtualizarLista() {
        carregarLista();
        showAlert(Alert.AlertType.INFORMATION, "Concluído", "Lista atualizada", "A lista de dinossauros foi atualizada.");
    }

    @FXML
    private void handleEditarSelecionado() {
        DinossauroDTO sel = tabelaDinossauros.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Nenhum item selecionado", "Selecione um dinossauro na lista.");
            return;
        }
        txtIdEdicao.setText(String.valueOf(sel.getId()));
        txtNomeEdicao.setText(Optional.ofNullable(sel.getNome()).orElse(""));
        txtEspecieEdicao.setText(Optional.ofNullable(sel.getEspecie()).orElse(""));
        txtPesoEdicao.setText(String.valueOf(sel.getPeso()));
        txtAlturaEdicao.setText(String.valueOf(sel.getAltura()));
        txtComprimentoEdicao.setText(String.valueOf(sel.getComprimento()));
        txtComportamentoEdicao.setText(Optional.ofNullable(sel.getComportamento()).orElse(""));

        tabPane.getSelectionModel().select(2);
    }

    @FXML
    private void handleExcluirSelecionadoFromList() {
        DinossauroDTO sel = tabelaDinossauros.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Nenhum item selecionado", "Selecione um dinossauro na lista.");
            return;
        }
        executarExclusao(sel.getId());
    }
    
    @FXML
    private void handleReorganizarIds() {
        try {
            dao.reorganizarIds();
            carregarLista();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "IDs Reorganizados", "Os IDs dos dinossauros foram reorganizados com sucesso.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao reorganizar IDs", e);
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível reorganizar os IDs.", e.getMessage());
        }
    }

    @FXML
    private void handleSalvarCadastro() {
        try {
            DinossauroDTO novo = new DinossauroDTO();
            novo.setNome(txtNomeCadastro.getText());
            novo.setEspecie(txtEspecieCadastro.getText());
            novo.setPeso(parseIntSafe(txtPesoCadastro.getText()));
            novo.setAltura(parseDoubleSafe(txtAlturaCadastro.getText()));
            novo.setComprimento(parseDoubleSafe(txtComprimentoCadastro.getText()));
            novo.setComportamento(txtComportamentoCadastro.getText());

            dao.inserir(novo);
            carregarLista();
            limparCamposCadastro();
            showAlert(Alert.AlertType.INFORMATION, "Concluído", "Cadastrado", "O dinossauro foi cadastrado com sucesso.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar dinossauro", e);
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível cadastrar.", e.getMessage());
        }
    }

    @FXML
    private void handleLimparCadastro() {
        limparCamposCadastro();
    }

    private void limparCamposCadastro() {
        txtNomeCadastro.clear();
        txtEspecieCadastro.clear();
        txtPesoCadastro.clear();
        txtAlturaCadastro.clear();
        txtComprimentoCadastro.clear();
        txtComportamentoCadastro.clear();
    }

    @FXML
    private void handleBuscarEdicao() {
        try {
            int id = parseIntSafe(txtIdEdicao.getText());
            List<DinossauroDTO> lista = dao.listar();
            for (DinossauroDTO d : lista) {
                if (d.getId() == id) {
                    txtNomeEdicao.setText(Optional.ofNullable(d.getNome()).orElse(""));
                    txtEspecieEdicao.setText(Optional.ofNullable(d.getEspecie()).orElse(""));
                    txtPesoEdicao.setText(String.valueOf(d.getPeso()));
                    txtAlturaEdicao.setText(String.valueOf(d.getAltura()));
                    txtComprimentoEdicao.setText(String.valueOf(d.getComprimento()));
                    txtComportamentoEdicao.setText(Optional.ofNullable(d.getComportamento()).orElse(""));
                    return;
                }
            }
            showAlert(Alert.AlertType.WARNING, "Não encontrado", "ID não localizado", "Nenhum dinossauro localizado.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Entrada inválida", "Erro", "Digite um ID numérico válido.");
        }
    }

    @FXML
    private void handleSalvarAlteracoes() {
        try {
            DinossauroDTO d = new DinossauroDTO();
            d.setId(parseIntSafe(txtIdEdicao.getText()));
            d.setNome(txtNomeEdicao.getText());
            d.setEspecie(txtEspecieEdicao.getText());
            d.setPeso(parseIntSafe(txtPesoEdicao.getText()));
            d.setAltura(parseDoubleSafe(txtAlturaEdicao.getText()));
            d.setComprimento(parseDoubleSafe(txtComprimentoEdicao.getText()));
            d.setComportamento(txtComportamentoEdicao.getText());

            dao.alterar(d);
            carregarLista();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Atualizado", "Os dados foram alterados.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar", e.getMessage());
        }
    }

    @FXML
    private void handleExcluirPorId() {
        try {
            int id = parseIntSafe(txtIdExclusao.getText());
            executarExclusao(id);
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Erro", "ID inválido", "Verifique o ID digitado.");
        }
    }

    @FXML
    private void handleCancelarEdicao() {
        txtIdEdicao.clear();
        txtNomeEdicao.clear();
        txtEspecieEdicao.clear();
        txtPesoEdicao.clear();
        txtAlturaEdicao.clear();
        txtComprimentoEdicao.clear();
        txtComportamentoEdicao.clear();
        tabPane.getSelectionModel().select(0);
    }

    @FXML
    private void handleExcluirSelecionadoFromExclusao() {
        DinossauroDTO sel = tabelaExclusao.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Nenhum item selecionado", "Selecione um dinossauro na lista.");
            return;
        }
        executarExclusao(sel.getId());
    }

    @FXML
    private void handleCancelarExclusao() {
        txtIdExclusao.clear();
        tabPane.getSelectionModel().select(0);
    }

    private void executarExclusao(int id) {
        try {
            dao.excluir(id);
            carregarLista();
            txtIdExclusao.clear();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Excluído", "Dinossauro removido com sucesso.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir", e);
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir.", e.getMessage());
        }
    }

    private int parseIntSafe(String val) {
        if (val == null || val.trim().isEmpty()) return 0;
        return Integer.parseInt(val.trim());
    }

    private double parseDoubleSafe(String val) {
        if (val == null || val.trim().isEmpty()) return 0.0;
        return Double.parseDouble(val.trim());
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
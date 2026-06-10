CREATE TABLE public.dinossauro (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    especie VARCHAR(255),
    peso INTEGER,
    altura DOUBLE PRECISION,
    comprimento DOUBLE PRECISION,
    comportamento TEXT,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE public.dinossauro IS 'Tabela para armazenar informações sobre dinossauros.';
COMMENT ON COLUMN public.dinossauro.id IS 'Identificador único do dinossauro.';
COMMENT ON COLUMN public.dinossauro.nome IS 'Nome do dinossauro.';
COMMENT ON COLUMN public.dinossauro.especie IS 'Espécie do dinossauro.';
COMMENT ON COLUMN public.dinossauro.peso IS 'Peso do dinossauro em quilogramas.';
COMMENT ON COLUMN public.dinossauro.altura IS 'Altura do dinossauro em metros.';
COMMENT ON COLUMN public.dinossauro.comprimento IS 'Comprimento do dinossauro em metros.';
COMMENT ON COLUMN public.dinossauro.comportamento IS 'Descrição do comportamento do dinossauro.';
COMMENT ON COLUMN public.dinossauro.data_criacao IS 'Data e hora em que o registro foi criado.';
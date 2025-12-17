package com.elias.gerenciador_tarefas_api.controller;

import com.elias.gerenciador_tarefas_api.model.Tarefa;
import com.elias.gerenciador_tarefas_api.service.TarefaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// @WebMvcTest foca-se APENAS na camada web. Não carrega repositórios nem banco de dados.
@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc; // A ferramenta que simula o browser/Insomnia

    @MockBean
    private TarefaService service; // Criamos um Mock do Service, pois não queremos testar a lógica dele aqui

    @Autowired
    private ObjectMapper objectMapper; // Transforma Objetos Java em JSON (e vice-versa)

    @Test
    @DisplayName("Deve criar tarefa com sucesso (200 OK)")
    void deveCriarTarefaComSucesso() throws Exception {
        // 1. ARRANGE
        Tarefa tarefaEnviada = new Tarefa();
        tarefaEnviada.setDescricao("Testar Controller");
        tarefaEnviada.setConcluida(false);

        // Simulamos o retorno do banco (com ID gerado)
        Tarefa tarefaRetornada = new Tarefa();
        tarefaRetornada.setId(1L);
        tarefaRetornada.setDescricao("Testar Controller");

        when(service.salvar(any(Tarefa.class))).thenReturn(tarefaRetornada);

        // 2. ACT & ASSERT
        mockMvc.perform(post("/tarefas") // Simula um POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefaEnviada))) // Converte objeto para JSON string
                .andExpect(status().isOk()) // Espera HTTP 200
                .andExpect(jsonPath("$.id").value(1)) // Espera que o JSON de volta tenha ID 1
                .andExpect(jsonPath("$.descricao").value("Testar Controller"));
    }

    @Test
    @DisplayName("Deve retornar Erro 400 (Bad Request) quando a descrição for vazia")
    void deveValidarDescricaoVazia() throws Exception {
        // 1. ARRANGE
        Tarefa tarefaInvalida = new Tarefa();
        tarefaInvalida.setDescricao(""); // Inválido! (Violando @NotBlank)

        // 2. ACT & ASSERT
        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefaInvalida)))
                .andExpect(status().isBadRequest()); // O Spring deve barrar antes de chegar no Service
    }

    @Test
    @DisplayName("Deve deletar tarefa com sucesso (204 No Content)")
    void deveDeletarComSucesso() throws Exception {
        // 1. ARRANGE
        Long idParaDeletar = 1L;
        when(service.deletar(idParaDeletar)).thenReturn(true); // Simula que o serviço conseguiu deletar

        // 2. ACT & ASSERT
        mockMvc.perform(delete("/tarefas/{id}", idParaDeletar))
                .andExpect(status().isNoContent()); // Espera HTTP 204
    }

    @Test
    @DisplayName("Deve retornar Erro 400 se tentar deletar tarefa concluída (Regra de Negócio)")
    void naoDeveDeletarTarefaBloqueada() throws Exception {
        // 1. ARRANGE
        Long idBloqueado = 2L;
        when(service.deletar(idBloqueado)).thenReturn(false); // O serviço diz: "Não deletei!"

        // 2. ACT & ASSERT
        mockMvc.perform(delete("/tarefas/{id}", idBloqueado))
                .andExpect(status().isBadRequest()); // Espera HTTP 400 (conforme programámos no Controller)
    }
}
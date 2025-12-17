package com.elias.gerenciador_tarefas_api.service;

import com.elias.gerenciador_tarefas_api.model.Tarefa;
import com.elias.gerenciador_tarefas_api.repository.TarefaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

// @ExtendWith diz ao JUnit para usar o Mockito para criar os "atores" falsos
@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    // @Mock: Cria o "ator" que finge ser o repositório (não conecta no banco real)
    @Mock
    private TarefaRepository repository;

    // @InjectMocks: Cria o Service verdadeiro e injeta o repositório falso dentro dele
    @InjectMocks
    private TarefaService service;

    @Test
    @DisplayName("Deve deletar a tarefa quando ela NÃO estiver concluída")
    void deveDeletarTarefaPendente() {
        // 1. ARRANGE (Preparar o Cenário)
        Long idExemplo = 1L;

        Tarefa tarefaPendente = new Tarefa();
        tarefaPendente.setId(idExemplo);
        tarefaPendente.setConcluida(false); // O cenário feliz: não está concluída

        // Ensinamos o Mock: "Quando buscarem pelo ID 1, retorna esta tarefa pendente"
        Mockito.when(repository.findById(idExemplo)).thenReturn(Optional.of(tarefaPendente));

        // 2. ACT (Executar a ação)
        boolean resultado = service.deletar(idExemplo);

        // 3. ASSERT (Verificar o resultado)
        // Esperamos que o resultado seja TRUE (conseguiu deletar)
        Assertions.assertTrue(resultado);

        // Verificamos se o método deleteById foi chamado no repositório 1 vez
        Mockito.verify(repository, Mockito.times(1)).deleteById(idExemplo);
    }

    @Test
    @DisplayName("NÃO deve deletar a tarefa quando ela ESTIVER concluída")
    void naoDeveDeletarTarefaConcluida() {
        // 1. ARRANGE (Preparar o Cenário de Erro)
        Long idExemplo = 2L;

        Tarefa tarefaConcluida = new Tarefa();
        tarefaConcluida.setId(idExemplo);
        tarefaConcluida.setConcluida(true); // O problema: já está concluída

        // Ensinamos o Mock: "Quando buscarem pelo ID 2, retorna esta tarefa concluída"
        Mockito.when(repository.findById(idExemplo)).thenReturn(Optional.of(tarefaConcluida));

        // 2. ACT (Executar a ação)
        boolean resultado = service.deletar(idExemplo);

        // 3. ASSERT (Verificar o resultado)
        // Esperamos que o resultado seja FALSE (foi bloqueado pela regra de negócio)
        Assertions.assertFalse(resultado);

        // O mais importante: Verificamos se o deleteById NUNCA foi chamado (proteção de dados)
        Mockito.verify(repository, Mockito.never()).deleteById(idExemplo);
    }
}
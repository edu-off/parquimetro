package br.com.parquimetro.component;

import br.com.parquimetro.enumerator.ModalidadesEstacionamento;
import br.com.parquimetro.service.EstacionamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleComponent {

    @Autowired
    private EstacionamentoService estacionamentoService;

    @Autowired
    @Qualifier("templateAlertMessageFixedTime")
    private SimpleMailMessage alertMessageFixedTimeMessage;

    @Autowired
    @Qualifier("templateAlertMessageByHour")
    private SimpleMailMessage alertMessageByHourMessage;

    @Scheduled(fixedRate = 300000)
    public void verificaEstacionamentosPorTempoFixo() {
        String modalidade = ModalidadesEstacionamento.TEMPO_FIXO.toString();
        estacionamentoService.verificaStatusEmAndamentoPorModalidade(alertMessageFixedTimeMessage, modalidade);
    }

    @Scheduled(fixedRate = 300000)
    public void verificaEstacionamentosPorHora() {
        String modalidade = ModalidadesEstacionamento.POR_HORA.toString();
        estacionamentoService.verificaStatusEmAndamentoPorModalidade(alertMessageByHourMessage, modalidade);
    }

}

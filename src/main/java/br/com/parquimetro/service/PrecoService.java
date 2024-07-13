package br.com.parquimetro.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PrecoService {

    private static final Double PRECO_POR_HORA = 20.00;

    protected double calculaPrecoPorHora(LocalDateTime entrada, LocalDateTime saida) {
        return PRECO_POR_HORA * entrada.until(saida, ChronoUnit.HOURS);
    }

    protected double calculaPrecoPorTempoFixo(Long tempoDuracao, LocalDateTime entrada, LocalDateTime saida) {
        return tempoDuracao < entrada.until(saida, ChronoUnit.HOURS) ?
                PRECO_POR_HORA * (tempoDuracao + (entrada.until(saida, ChronoUnit.HOURS) - tempoDuracao)):
                PRECO_POR_HORA * tempoDuracao;
    }

}

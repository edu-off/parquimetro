package br.com.parquimetro.component;

import org.springframework.stereotype.Component;

@Component
public class TextBuilderComponent {

    private StringBuilder builder = new StringBuilder();

    public String subjectAlertMessageFixedTime() {
        builder.setLength(0);
        builder.append("Alerta de diária de estacionamento por tempo fixo");
        return builder.toString();
    }

    public String subjectAlertMessageByHour() {
        builder.setLength(0);
        builder.append("Alerta de diária de estacionamento por hora");
        return builder.toString();
    }

    public String subjectPaymentMessage() {
        builder.setLength(0);
        builder.append("Recibo de pagamento");
        return builder.toString();
    }

    public String textAlertMessageFixedTime() {
        builder.setLength(0);
        builder.append("Atenção: diária de estacionamento prestes a expirar.")
                .append(System.lineSeparator()).append("Dados de entrada: %s ")
                .append(System.lineSeparator()).append("Previsão para saída: %s ")
                .append(System.lineSeparator()).append(System.lineSeparator())
                .append("Atenciosamente, parquimentos LTDA");
        return builder.toString();
    }

    public String textAlertMessageByHour() {
        builder.setLength(0);
        builder.append("Atenção: a diária será estendida para mais uma hora.")
                .append(System.lineSeparator()).append("Dados de entrada: %s ")
                .append(System.lineSeparator()).append("Previsão para saída: %s ")
                .append(System.lineSeparator()).append(System.lineSeparator())
                .append("Atenciosamente, parquimentos LTDA");
        return builder.toString();
    }

    public String textPaymentMessage() {
        builder.setLength(0);
        builder.append("Pagamento efetuado referente a diária de estacionamento, no valor de %s. Abaixo, seguem informações detalhadas: ")
                .append(System.lineSeparator()).append("Dados de entrada: %s ")
                .append(System.lineSeparator()).append("Dados de saída: %s ")
                .append(System.lineSeparator()).append("Nome do condutor: %s ")
                .append(System.lineSeparator()).append("Veiculo: %s ")
                .append(System.lineSeparator()).append("Placa: %s ")
                .append(System.lineSeparator()).append(System.lineSeparator())
                .append("Atenciosamente, parquimentos LTDA");
        return builder.toString();
    }

}

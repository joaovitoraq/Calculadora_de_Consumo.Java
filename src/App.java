import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class App {
    public static void main(String[] args) {
        int numeroAparelhos = Integer.parseInt(JOptionPane.showInputDialog("Quantos aparelhos deseja calcular o consumo?"));

        ArrayList<App> aparelhos = new ArrayList<>();

        for (int i = 0; i < numeroAparelhos; i++) {
            App app = new App();
            app.perguntaDetalhes();
            aparelhos.add(app);
        }

        // Calcula o gasto total do mês
        double gastoTotal = 0;
        for (App aparelho : aparelhos) {
            gastoTotal += aparelho.valor;
        }

        // Imprime as informações de todos os aparelhos
        StringBuilder mensagem = new StringBuilder();
        for (App aparelho : aparelhos) {
            mensagem.append(aparelho.getInfo()).append("\n\n");
        }

        mensagem.append("Gasto total do mês: ").append(new DecimalFormat("#.##").format(gastoTotal));

        JOptionPane.showMessageDialog(null, mensagem.toString());
    }

    String aparelho;
    double watts;
    double temp_uso;
    int diasMes;
    double valor;
    DecimalFormat df = new DecimalFormat("#.##");
    String valorFormatado;

    public void perguntaDetalhes() {
        this.aparelho = JOptionPane.showInputDialog("Nome do aparelho:");

        if (aparelho == null || aparelho.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum nome de aparelho fornecido.");
            System.exit(0);
        } else if (!aparelho.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(null, "O nome do aparelho deve conter apenas letras.");
            System.exit(0);
        }

        String wattsInput = JOptionPane.showInputDialog("Digite os Watts do seu aparelho:");
        this.watts = Double.parseDouble(wattsInput); // potência em Watts do aparelho

        String tempInput = JOptionPane.showInputDialog("Digite o tempo de uso (Hora):");
        this.temp_uso = Double.parseDouble(tempInput); // tempo em horas que esse aparelho é usado

        this.diasMes = Integer.parseInt(JOptionPane.showInputDialog("Dias do Mês (uso):"));

        this.valor = ((watts * temp_uso * diasMes / 1000) * 0.709);

        this.valorFormatado = df.format(valor);
    }

    public String getInfo() {
        return "Nome do aparelho: " + aparelho + "\n" +
                "Tempo médio de uso: " + temp_uso + " horas\n" +
                "Dias por mês: " + diasMes + "Dias\n" +
                "Consome: " + watts + " Watts \n" +
                "Gasto mensal: " + valorFormatado;
    }
}

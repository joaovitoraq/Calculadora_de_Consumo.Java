import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static Map<String, Double> tarifasEstados = new HashMap<>();

    static {
        // Associa cada estado à sua respectiva tarifa
        tarifasEstados.put("Pará", 0.962);
        tarifasEstados.put("Mato Grosso", 0.883);
        tarifasEstados.put("Mato Grosso do Sul", 0.88);
        tarifasEstados.put("Alagoas", 0.866);
        tarifasEstados.put("Piauí", 0.854);
        tarifasEstados.put("Rio de Janeiro", 0.84);
        tarifasEstados.put("Amazonas", 0.835);
        tarifasEstados.put("Acre", 0.828);
        tarifasEstados.put("Bahia", 0.808);
        tarifasEstados.put("Distrito Federal", 0.766);
        tarifasEstados.put("Pernambuco", 0.764);
        tarifasEstados.put("Tocantins", 0.756);
        tarifasEstados.put("Minas Gerais", 0.751);
        tarifasEstados.put("Ceará", 0.744);
        tarifasEstados.put("Roraima", 0.735);
        tarifasEstados.put("Maranhão", 0.719);
        tarifasEstados.put("Rondônia", 0.709);
        tarifasEstados.put("Goiás", 0.711);
        tarifasEstados.put("Espírito Santo", 0.696);
        tarifasEstados.put("Rio Grande do Sul", 0.691);
        tarifasEstados.put("Rio Grande do Norte", 0.689);
        tarifasEstados.put("São Paulo", 0.68);
        tarifasEstados.put("Sergipe", 0.651);
        tarifasEstados.put("Paraná", 0.639);
        tarifasEstados.put("Paraíba", 0.602);
        tarifasEstados.put("Santa Catarina", 0.593);
    }

    private static double tarifaSelecionada = 0.709; // Valor padrão caso o estado não seja selecionado

    public static void main(String[] args) {
        selecionarEstado(); // Chama o método para selecionar o estado antes de prosseguir

    }

    private static void selecionarEstado() {
        // Cria um painel para exibir a seleção de estado
        JPanel selecionar = new JPanel();
        JComboBox<String> estadosComboBox = new JComboBox<>(tarifasEstados.keySet().toArray(new String[0]));
        selecionar.add(new JLabel("Selecione seu estado:"));
        selecionar.add(estadosComboBox);

        // Exibe um diálogo de seleção de estado
        int result = JOptionPane.showConfirmDialog(null, selecionar, "Selecione seu estado", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) { // Se o usuário clicou em OK
            // Atualiza a tarifa selecionada com base no estado selecionado
            String estadoSelecionado = (String) estadosComboBox.getSelectedItem();
            tarifaSelecionada = tarifasEstados.get(estadoSelecionado);
        } else { // Se o usuário cancelou, saia do programa
            System.exit(0);
        }

        int numeroAparelhos = 0; // Inicialize com um valor padrão

        while (true) {
            String input = JOptionPane.showInputDialog("Quantos aparelhos deseja calcular o consumo?");
            if (input == null) { // Usuário cancelou
                System.exit(0); // Saia do programa
            }

            try {
                numeroAparelhos = Integer.parseInt(input);
                if (numeroAparelhos <= 0) {
                    JOptionPane.showMessageDialog(null, "O número de aparelhos deve ser um inteiro positivo.");
                } else {
                    break; // Se a entrada for válida, saia do loop
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido de aparelhos.");
            }
        }

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

        // Adiciona o botão de exportar
        JButton exportButton = new JButton("Exportar para TXT");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarParaTXT(mensagem.toString());
            }
        });

        // Cria um painel para adicionar o botão
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(new JTextArea(mensagem.toString())));
        panel.add(exportButton);

        // Mostra a mensagem em uma janela com scroll
        JOptionPane.showMessageDialog(null, panel);
    }

    private static void exportarParaTXT(String texto) {
        try {
            // Diretório do Desktop
            String desktopDir = System.getProperty("user.home") + "/Desktop";
            // Cria o arquivo
            File file = new File(desktopDir, "Calculo_consumo.txt");
            // Escreve o texto no arquivo
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(texto);
            writer.close();
            JOptionPane.showMessageDialog(null, "Texto exportado para o arquivo Calculo_consumo.txt na área de trabalho.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exportar o texto para o arquivo.");
        }
    }

    private String aparelho;
    private double watts;
    private double temp_uso;
    private int diasMes;
    private double valor;
    private String valorFormatado;
    private DecimalFormat df = new DecimalFormat("#.##");

    public void perguntaDetalhes() {
        while (true) {
            this.aparelho = JOptionPane.showInputDialog("Nome do aparelho:");
            if (this.aparelho == null) { // Usuário cancelou
                System.exit(0); // Saia do programa
            } else if (this.aparelho.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum nome de aparelho fornecido.");
            } else if (!this.aparelho.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(null, "O nome do aparelho deve conter apenas letras.");
            } else {
                break; // Se o nome do aparelho for válido, saia do loop
            }
        }

        while (true) {
            try {
                String wattsInput = JOptionPane.showInputDialog("Digite os Watts do seu aparelho:");
                if (wattsInput == null) { // Usuário cancelou
                    System.exit(0); // Saia do programa
                }
                this.watts = Double.parseDouble(wattsInput); // potência em Watts do aparelho
                break; // Se a entrada for válida, saia do loop
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido para os Watts.");
            }
        }

        while (true) {
            try {
                String tempInput = JOptionPane.showInputDialog("Digite o tempo de uso (Horas):");
                if (tempInput == null) { // Usuário cancelou
                    System.exit(0); // Saia do programa
                }
                this.temp_uso = Double.parseDouble(tempInput); // tempo em horas que esse aparelho é usado
                break; // Se a entrada for válida, saia do loop
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido para o tempo de uso.");
            }
        }

        while (true) {
            try {
                String diasInput = JOptionPane.showInputDialog("Dias do Mês (Uso):");
                if (diasInput == null) { // Usuário cancelou
                    System.exit(0); // Saia do programa
                }
                this.diasMes = Integer.parseInt(diasInput);
                if (this.diasMes <= 0) {
                    JOptionPane.showMessageDialog(null, "O número de dias do mês deve ser um inteiro positivo.");
                } else {
                    break; // Se a entrada for válida, saia do loop
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido para os dias do mês.");
            }
        }

        this.valor = ((watts * temp_uso * diasMes / 1000) * 0.709);

        this.valorFormatado = df.format(valor);
    }

    public String getInfo() {
        return "Nome do aparelho: " + aparelho + "\n" +
                "Tempo médio de uso: " + temp_uso + " horas\n" +
                "Dias por mês: " + diasMes + " dias\n" +
                "Consome: " + watts + " Watts \n" +
                "Tarifa: " + tarifaSelecionada + "Kw/h\n"+
                "Gasto mensal: " + valorFormatado;
    }
}
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainApp {
    public static void main(String[] args) {
        new Controller().run();
    }
}

class Controller {
    private TarifaService tarifaService;
    private UserInterface userInterface;
    private ExportService exportService;
    private ProdutoService produtoService;

    public Controller() {
        this.tarifaService = new TarifaService();
        this.userInterface = new UserInterface();
        this.exportService = new ExportService();
        this.produtoService = new ProdutoService();
    }

    public void run() {
        Estado estado = userInterface.selecionarEstado(tarifaService.getTarifasEstados());
        if (estado == null) return;

        while (true) {
            String[] options = {"Cadastrar novo aparelho", "Calcular consumo", "Editar aparelho existente", "Sair"};
            int choice = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                Aparelho novoAparelho = userInterface.perguntarDetalhesAparelho();
                if (novoAparelho != null) {
                    produtoService.cadastrarAparelho(novoAparelho);
                }
            } else if (choice == 1) {
                int numeroAparelhos = userInterface.perguntarNumeroAparelhos();
                if (numeroAparelhos <= 0) continue;

                ArrayList<Aparelho> aparelhos = new ArrayList<>();
                for (int i = 0; i < numeroAparelhos; i++) {
                    Aparelho aparelho = userInterface.selecionarOuCadastrarAparelho(produtoService);
                    if (aparelho != null) {
                        aparelho.calcularGasto(estado.getTarifa());
                        aparelhos.add(aparelho);
                    }
                }

                double gastoTotal = 0;
                StringBuilder mensagem = new StringBuilder();
                for (Aparelho aparelho : aparelhos) {
                    gastoTotal += aparelho.getValor();
                    mensagem.append(aparelho.getInfo(estado.getTarifa())).append("\n\n");
                }

                mensagem.append("Gasto total do mês: ").append(new DecimalFormat("#.##").format(gastoTotal));
                userInterface.mostrarResultado(mensagem.toString(), exportService);
            } else if (choice == 2) {
                Aparelho aparelho = userInterface.selecionarAparelhoExistente(produtoService);
                if (aparelho != null) {
                    userInterface.editarAparelho(aparelho);
                    produtoService.salvarAparelhos();
                }
            } else {
                break;
            }
        }
    }
}

class TarifaService {
    private Map<String, Double> tarifasEstados;

    public TarifaService() {
        tarifasEstados = new HashMap<>();
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

    public Map<String, Double> getTarifasEstados() {
        return tarifasEstados;
    }
}

class UserInterface {
    public Estado selecionarEstado(Map<String, Double> tarifasEstados) {
        JPanel selecionar = new JPanel();
        JComboBox<String> estadosComboBox = new JComboBox<>(tarifasEstados.keySet().toArray(new String[0]));
        selecionar.add(new JLabel("Selecione seu estado:"));
        selecionar.add(estadosComboBox);

        int result = JOptionPane.showConfirmDialog(null, selecionar, "Selecione seu estado", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String estadoSelecionado = (String) estadosComboBox.getSelectedItem();
            return new Estado(estadoSelecionado, tarifasEstados.get(estadoSelecionado));
        } else {
            return null;
        }
    }

    public int perguntarNumeroAparelhos() {
        while (true) {
            String input = JOptionPane.showInputDialog("Quantos aparelhos deseja calcular o consumo?");
            if (input == null) {
                return -1;
            }

            try {
                int numeroAparelhos = Integer.parseInt(input);
                if (numeroAparelhos <= 0) {
                    JOptionPane.showMessageDialog(null, "O número de aparelhos deve ser um inteiro positivo.");
                } else {
                    return numeroAparelhos;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido de aparelhos.");
            }
        }
    }

    public Aparelho perguntarDetalhesAparelho() {
        String nome = null;
        while (true) {
            nome = JOptionPane.showInputDialog("Nome do aparelho:");
            if (nome == null) {
                return null;
            } else if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum nome de aparelho fornecido.");
            } else if (!nome.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(null, "O nome do aparelho deve conter apenas letras.");
            } else {
                break;
            }
        }

        double watts = perguntarNumero("Digite os Watts do seu aparelho:");
        if (watts < 0) return null;

        double tempUso = perguntarNumero("Digite o tempo de uso (Horas):");
        if (tempUso < 0) return null;

        int diasMes = (int) perguntarNumero("Dias do Mês (Uso):");
        if (diasMes < 0) return null;

        return new Aparelho(nome, watts, tempUso, diasMes);
    }

    private double perguntarNumero(String mensagem) {
        while (true) {
            String input = JOptionPane.showInputDialog(mensagem);
            if (input == null) {
                return -1;
            }

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido.");
            }
        }
    }

    public Aparelho selecionarOuCadastrarAparelho(ProdutoService produtoService) {
        String[] options = {"Selecionar aparelho existente", "Cadastrar novo aparelho"};
        int choice = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Aparelhos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            return selecionarAparelhoExistente(produtoService);
        } else {
            return perguntarDetalhesAparelho();
        }
    }

    public Aparelho selecionarAparelhoExistente(ProdutoService produtoService) {
        ArrayList<Aparelho> aparelhos = produtoService.getAparelhos();
        if (aparelhos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum aparelho cadastrado. Cadastre um novo aparelho.");
            return null;
        }

        String[] nomesAparelhos = aparelhos.stream().map(Aparelho::getNome).toArray(String[]::new);
        String nomeSelecionado = (String) JOptionPane.showInputDialog(null, "Selecione um aparelho:", "Aparelhos",
                JOptionPane.QUESTION_MESSAGE, null, nomesAparelhos, nomesAparelhos[0]);

        return aparelhos.stream().filter(ap -> ap.getNome().equals(nomeSelecionado)).findFirst().orElse(null);
    }

    public void editarAparelho(Aparelho aparelho) {
        double watts = perguntarNumero("Digite os Watts do seu aparelho:", aparelho.getWatts());
        if (watts < 0) return;

        double tempUso = perguntarNumero("Digite o tempo de uso (Horas):", aparelho.getTempUso());
        if (tempUso < 0) return;

        int diasMes = (int) perguntarNumero("Dias do Mês (Uso):", aparelho.getDiasMes());
        if (diasMes < 0) return;

        aparelho.setWatts(watts);
        aparelho.setTempUso(tempUso);
        aparelho.setDiasMes(diasMes);
    }

    private double perguntarNumero(String mensagem, double valorAtual) {
        while (true) {
            String input = JOptionPane.showInputDialog(mensagem, valorAtual);
            if (input == null) {
                return -1;
            }

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número válido.");
            }
        }
    }

    public void mostrarResultado(String mensagem, ExportService exportService) {
        JButton exportButton = new JButton("Exportar para TXT");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportService.exportarParaTXT(mensagem);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(new JTextArea(mensagem)));
        panel.add(exportButton);

        JOptionPane.showMessageDialog(null, panel);
    }
}

class ExportService {
    public void exportarParaTXT(String texto) {
        try {
            String desktopDir = System.getProperty("user.home") + "/Desktop";
            File file = new File(desktopDir, "Calculo_consumo.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(texto);
            writer.close();
            JOptionPane.showMessageDialog(null, "Texto exportado para o arquivo Calculo_consumo.txt na área de trabalho.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao exportar o texto para o arquivo.");
        }
    }
}

class ProdutoService {
    private ArrayList<Aparelho> aparelhos;
    private final String arquivo = "aparelhos.dat";

    public ProdutoService() {
        aparelhos = carregarAparelhos();
    }

    public void cadastrarAparelho(Aparelho aparelho) {
        aparelhos.add(aparelho);
        salvarAparelhos();
    }

    public ArrayList<Aparelho> getAparelhos() {
        return aparelhos;
    }

    public void salvarAparelhos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(aparelhos);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar aparelhos.");
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Aparelho> carregarAparelhos() {
        File file = new File(arquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<Aparelho>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar aparelhos.");
            return new ArrayList<>();
        }
    }
}

class Estado {
    private String nome;
    private double tarifa;

    public Estado(String nome, double tarifa) {
        this.nome = nome;
        this.tarifa = tarifa;
    }

    public String getNome() {
        return nome;
    }

    public double getTarifa() {
        return tarifa;
    }
}

class Aparelho implements Serializable {
    private String nome;
    private double watts;
    private double tempUso;
    private int diasMes;
    private double valor;
    private String valorFormatado;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public Aparelho(String nome, double watts, double tempUso, int diasMes) {
        this.nome = nome;
        this.watts = watts;
        this.tempUso = tempUso;
        this.diasMes = diasMes;
    }

    public void calcularGasto(double tarifa) {
        valor = watts * tempUso * diasMes * tarifa / 1000;
        valorFormatado = decimalFormat.format(valor);
    }

    public double getValor() {
        return valor;
    }

    public String getNome() {
        return nome;
    }

    public double getWatts() {
        return watts;
    }

    public void setWatts(double watts) {
        this.watts = watts;
    }

    public double getTempUso() {
        return tempUso;
    }

    public void setTempUso(double tempUso) {
        this.tempUso = tempUso;
    }

    public int getDiasMes() {
        return diasMes;
    }

    public void setDiasMes(int diasMes) {
        this.diasMes = diasMes;
    }

    public String getInfo(double tarifa) {
        return String.format("Aparelho: %s\nWatts: %.2f\nTempo de uso diário: %.2f horas\nDias de uso no mês: %d\nGasto mensal: R$%s",
                nome, watts, tempUso, diasMes, valorFormatado);
    }
}

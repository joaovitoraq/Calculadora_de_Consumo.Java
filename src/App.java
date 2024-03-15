import java.text.DecimalFormat;
import javax.swing.JOptionPane;

public class App{
    public static void main(String[] args) {
        App app;
        app = new App ();
        
        app.imprimeDados();  
      }
    
    String aparelho = JOptionPane.showInputDialog("Nome do aparelho:");{

        if (aparelho == null || aparelho.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum nome de aparelho fornecido.");
            System.exit(0);
        } else if (!aparelho.matches("[a-zA-Z]+")) {
          JOptionPane.showMessageDialog(null, "O nome do aparelho deve conter apenas letras.");
          System.exit(0);
        } else;
      
      }

    String wattsInput = JOptionPane.showInputDialog("Digite os Watts do seu aparelho:");
    double watts = Double.parseDouble(wattsInput); // potência em Watts do aparelho

    String tempInput = JOptionPane.showInputDialog("Digite o tempo de uso (Hora):");
    double temp_uso = Double.parseDouble(tempInput); // tempo em horas que esse aparelho é usado

    int diasMes = Integer.parseInt(JOptionPane.showInputDialog("Dias do Mês (uso):"));

    public double valor = ((watts * temp_uso * diasMes / 1000) * 0.709);

    //int a = Integer.parseInt(JOptionPane.showInputDialog(""));
    DecimalFormat df = new DecimalFormat("#.##");
    String valorFormatado = df.format(valor);

    void imprimeDados(){

        String mensagem = "Nome do aparelho: " + aparelho + "\n" +
                          "Tempo médio de uso: " + temp_uso + " horas\n" +
                          "Consome: " + watts + " Watts \n" +
                          "Gasto mensal: " + valorFormatado;
        JOptionPane.showMessageDialog(null, mensagem); 
      }
    }
    
    

  



       


  


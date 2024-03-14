import java.text.DecimalFormat;
import javax.swing.JOptionPane;

public class App{
    public static void main(String[] args) {
        App app;
        app = new App ();
        
        app.imprimeDados();
      }

    String aparelho = JOptionPane.showInputDialog("Nome do aparelho:"); // Nome do aparelho

    String tempInput = JOptionPane.showInputDialog("Digite o tempo de uso (Hora):");
    double temp_uso = Double.parseDouble(tempInput); // tempo em horas que esse aparelho é usado

    String kwInput = JOptionPane.showInputDialog("Digite o consumo Kw/h do aparelho:");
    double kwhora = Double.parseDouble(kwInput); // quanto esse apareho gasta por hora

    public double valor = ((kwhora * 0.71) * temp_uso) * 30;

    //int a = Integer.parseInt(JOptionPane.showInputDialog(""));
    DecimalFormat df = new DecimalFormat("#.##");
    String valorFormatado = df.format(valor);

    void imprimeDados(){
        /*System.out.println("Aparelho: " +this.aparelho);
        System.out.println("Tempo médio de uso: " +this.temp_uso);
        //System.out.println("limite:" +this.limite);
        System.out.println("Consome: " +this.kwhora);

        System.out.println("Gasto mensal: " +this.valorFormatado);*/

        //JOptionPane.showMessageDialog(null, "Gasto mensal: " + valorFormatado);

        String mensagem = "Nome do aparelho: " + aparelho + "\n" +
                          "Tempo médio de uso: " + temp_uso + " horas\n" +
                          "Consome: " + kwhora + " Kw/h\n" +
                          "Gasto mensal: " + valorFormatado;
        JOptionPane.showMessageDialog(null, mensagem);
    }
    
}

  


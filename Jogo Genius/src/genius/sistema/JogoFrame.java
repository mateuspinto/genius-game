/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genius.sistema;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

/**
 *
 * @author Geovani
 */
public class JogoFrame extends JFrame{
    
    private JButton vermelho;
    private JButton azul;
    private JButton verde;
    private JButton amarelo;
    private JLabel titulo;
    
    private Timer timerPisca;
    private Timer timerUser;
    
    ArrayList<Integer> listaCores = new ArrayList<Integer>();
    ArrayList<Integer> listaUser = new ArrayList<Integer>();
    int x = 1, y = 0, z = 0, w = 0;
    boolean fim = false;
    long start, finish, total = 0;
    
    public JogoFrame(){
        super("Genius");
        
        criarLayout();
    }
    
    private void criarLayout(){
        setLayout(new BorderLayout());
        
        SairAction sairAction = new SairAction();
        NovoAction novoAction = new NovoAction();
        BotaoAction botaoAction = new BotaoAction();
        
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new FlowLayout());
        
        titulo = new JLabel("GENIUS");
        titulo.setFont(new Font("Verdana", Font.PLAIN, 16));
        
        panelTitulo.add(titulo);
        
        JPanel botoes = new JPanel();
        botoes.setLayout(new FlowLayout());
        
        vermelho = new JButton("vermelho");
        vermelho.setBackground(Color.red);
        vermelho.addActionListener(botaoAction);
        azul = new JButton("azul");
        azul.setBackground(Color.blue);
        azul.addActionListener(botaoAction);
        verde = new JButton("verde");
        verde.setBackground(Color.green);
        verde.addActionListener(botaoAction);
        amarelo = new JButton("amarelo");
        amarelo.setBackground(Color.yellow);
        amarelo.addActionListener(botaoAction);
        
        botoes.add(vermelho);
        botoes.add(azul);
        botoes.add(verde);
        botoes.add(amarelo);
        
        JPanel iniciar = new JPanel();
        botoes.setLayout(new FlowLayout());
        
        JButton inicia = new JButton("iniciar/reiniciar");
        inicia.addActionListener(novoAction);
        
        JButton sair = new JButton("Sair");
        sair.addActionListener(sairAction);
        
        iniciar.add(inicia);
        iniciar.add(sair);
        
        add(panelTitulo, BorderLayout.NORTH);
        add(botoes, BorderLayout.CENTER);
        add(iniciar, BorderLayout.SOUTH);
    }
    
    public void piscar(int cor) {
    SwingWorker worker = new SwingWorker() {
        @Override
        protected Object doInBackground() throws Exception {
            switch (cor) {
                case 0:
                    azul.setBackground(Color.GRAY);
                    Thread.sleep(500);
                    azul.setBackground(Color.BLUE);
                    break;
                case 1:
                    vermelho.setBackground(Color.GRAY);
                    Thread.sleep(500);
                    vermelho.setBackground(Color.RED);
                    break;
                case 2:
                    amarelo.setBackground(Color.GRAY);
                    Thread.sleep(500);
                    amarelo.setBackground(Color.YELLOW);
                    break;
                case 3:
                    verde.setBackground(Color.GRAY);
                    Thread.sleep(500);
                    verde.setBackground(Color.GREEN);
                    break;
            }
            return null;
        }

        @Override
        protected void done() {

            super.done();
            try {
                get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
    };
    worker.execute();
    }
    
    private class NovoAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            Random geradorNumeros = new Random();
            int numeroGerado, j;
            PiscaAction piscaAction = new PiscaAction();
            
            for(j = 0; j < 8; j++){
                numeroGerado = geradorNumeros.nextInt(4);
                listaCores.add(numeroGerado);
            }
            timerPisca = new Timer(1000, piscaAction);   
            timerPisca.start();
        }
    }
    
    private class PiscaAction implements ActionListener{ 
        public void actionPerformed(ActionEvent event){  
            UserAction userAction = new UserAction();
            if(y < x){
                piscar(listaCores.get(y));
                y++;
            }
            if(y==x && fim == false){
                titulo.setText("Insira a sequencia" + y);
                titulo.setFont(new Font("Verdana", Font.PLAIN, 16));
                start = System.currentTimeMillis();
                timerUser = new Timer(5000, userAction);  
                timerUser.start();
                finish = System.currentTimeMillis();
                total = finish - start;
            }
            if(x == 9){
                titulo.setText("Você Ganhou. Parabéns!!!" );
                titulo.setFont(new Font("Verdana", Font.PLAIN, 25));
                System.exit(0);
            }
            if(fim == true || total > 5000){
                titulo.setText("GAME OVER" );
                titulo.setFont(new Font("Verdana", Font.PLAIN, 25));
                System.exit(0);
            }
        }
    }
    
    private class UserAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){ 
            int i;
            if(z == x){
                for(i=0; i < x; i++){
                    if(listaCores.get(i) == listaUser.get(i)){
                        titulo.setText("Acertou!!!" );
                        titulo.setFont(new Font("Verdana", Font.PLAIN, 16));
                    } else {
                        titulo.setText("Errou!!!" );
                        titulo.setFont(new Font("Verdana", Font.PLAIN, 16));
                        fim = true;
                        timerUser.stop();
                    }
                }
                if (fim == false){
                    listaUser.clear();
                    x++;
                    z = 0;
                    y = 0;
                    timerUser.stop();
                }
            }
            if(z == 9){
                fim = true;
                timerUser.stop();
            }
        }
    }
    
    private class SairAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            System.exit(0);
        }
    }
    
    private class BotaoAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
             if(ae.getSource().equals(azul)){
                 listaUser.add(0);
                 z++;

             }else if(ae.getSource().equals(vermelho)){
                 listaUser.add(1);
                 z++;

             }else if(ae.getSource().equals(amarelo)){
                 listaUser.add(2);
                 z++;

             }else if(ae.getSource().equals(verde)){
                 listaUser.add(3);
                 z++;
             }
        }
    }
    
}

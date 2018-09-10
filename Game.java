import javax.swing.*;
import java.awt.*;

public class Game extends JFrame implements FieldParent
{

	private static final long serialVersionUID = 1L;
	
	private JTextField scoreField = new JTextField(4);
    private static final String HELP = "Arrow keys - moving Tetrominoes\n" +
                                        "a, d - roatating Tetrominoes\n" +
                                        "p - pause";
    public Game(Field field)
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Tetris");

        JMenuBar menuBar = new JMenuBar();

        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem help = new JMenuItem("Help");
        JMenuItem exit = new JMenuItem("Exit");

        JLabel scoreLabel = new JLabel("Score:");

        setLayout(new FlowLayout());

        menuBar.add(newGame);
        menuBar.add(help);
        menuBar.add(exit);
        setJMenuBar(menuBar);

        newGame.addActionListener(event ->
        {
            field.reset();
            field.start();
        });
        help.addActionListener(event ->
        {
            JOptionPane.showMessageDialog(this, HELP, "Help", JOptionPane.INFORMATION_MESSAGE);
        });
        exit.addActionListener(event ->
        {
            System.exit(0);
        });

        scoreField.setEditable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(scoreLabel);
        panel.add(scoreField);

        add(field);
        add(panel);

        pack();
    }

    public void updateScore(int sc)
    {
        scoreField.setText("" + sc);
    }
}
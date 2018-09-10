import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Field extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	private final int width;
    private final int height;
    private final int pixelSize; //size of one element of every brick

    private int[][] field; //table which has information about content of Tetris field

    private Brick movingBrick;
    private Brick nextBrick;

    private Timer timer;

    private boolean isFinished = false; //this variable is set true when game ends, and false when game begins again
    private boolean isStarted = false; //this variable is set true when game starts for the first time and from then is never changed
    private boolean isPaused = false; //this variable is set true when game is paused

    private int score = 0;

    private FieldParent parent; //object which must be informed about changing a score
                                //this field is needed because class doesn't provide score presentation

    public Field(int pixelSize, int width, int height, int movementSpeed)
    {
        this.pixelSize =pixelSize;
        this.width = width;
        this.height = height;
        field =  new int[width][height];

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(pixelSize*width, pixelSize*height));

        for(int i = 0; i < width; i++)
        {
            Arrays.fill(field[i], -1); //-1 means that on this place there isn't any brick
        }

        movingBrick = new Brick(5, 1);
        nextBrick = new Brick(5, 1);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
            	
                int key = e.getKeyCode();
                if(!isPaused) {
                    if (key == KeyEvent.VK_LEFT) {
                        if (!isPossibleCollision(movingBrick.getCoords(), movingBrick.getxPos() - 1, movingBrick.getyPos()))
                            movingBrick.setxPos(movingBrick.getxPos() - 1);
                    } else if (key == KeyEvent.VK_RIGHT) {
                        if (!isPossibleCollision(movingBrick.getCoords(), movingBrick.getxPos() + 1, movingBrick.getyPos()))
                            movingBrick.setxPos(movingBrick.getxPos() + 1);
                    } else if (key == KeyEvent.VK_DOWN) {
                        if (!isPossibleCollision(movingBrick.getCoords(), movingBrick.getxPos(), movingBrick.getyPos() + 1))
                            movingBrick.setyPos(movingBrick.getyPos() + 1);
                    } else if (key == KeyEvent.VK_A) {
                        int[][] coords = movingBrick.rotateLeft();
                        if (!isPossibleCollision(coords, movingBrick.getxPos(), movingBrick.getyPos()))
                            movingBrick.setCoords(coords);
                    } else if (key == KeyEvent.VK_D) {
                        int[][] coords = movingBrick.rotateRight();
                        if (!isPossibleCollision(coords, movingBrick.getxPos(), movingBrick.getyPos()))
                            movingBrick.setCoords(coords);
                    }
                }
                if(key == KeyEvent.VK_P)
                {
                    if(isPaused)
                    {
                        isPaused = false;
                        timer.start();
                    }
                    else
                    {
                        isPaused = true;
                        timer.stop();
                    }
                }

                repaint();
            }
        });
        
        //this part is responsible for moving a brick on the field
        timer = new Timer(movementSpeed, event ->
        {
            if(isPossibleCollision(movingBrick.getCoords(), movingBrick.getxPos(), movingBrick.getyPos() + 1))
            {
                addBrick();
                if(isFinished)
                    timer.stop();
            }
            else
                movingBrick.setyPos(movingBrick.getyPos()+1);
            removeFilledLevels();
            repaint();
        });

        setFocusable(true);
        requestFocus();
        repaint();
    }
    
    public void setFieldParent(FieldParent parent) {
    	this.parent = parent;
    }

    private boolean isPossibleCollision(int[][] coord, int xPos, int yPos) //this method checks if brick with given
    {                                                                      //coordinates collides with other bricks or walls
        boolean isPosCol = false;
        for(int i = 0; i < 4; i++)
        {
            if(coord[i][0] + xPos >= width || coord[i][0] + xPos < 0 || 
            	yPos - coord[i][1]  >= height || field[coord[i][0]+xPos][yPos - coord[i][1]] >= 0)
            {
                isPosCol = true;
                break;
            }
        }
        return isPosCol;
    }

    private void addBrick() //adds new brick to the table when movingBrick can't move down
    {
        int[][] temp = movingBrick.getElementsPosition();
        int shape = movingBrick.getShapeNumber();
        for(int i = 0; i < 4; i++)
        {
            field[temp[i][0]][temp[i][1]] = shape;
        }
        temp = nextBrick.getElementsPosition();
        for(int i = 0; i < 4; i++)
        {
            if(field[temp[i][0]][temp[i][1]] >= 0)
            {
                isFinished = true;
                break;
            }
        }
        if(!isFinished)
        {
            movingBrick = nextBrick;
            nextBrick = new Brick(5, 1);
        }
    }

    private void removeFilledLevels() //checks if some levels are filled and can be removed
    {
        boolean isFilled;
        for(int i = 0; i < height; i++)
        {
            isFilled = true;
            for(int j = 0; j < width; j++)
            {
                if(field[j][i] < 0)
                {
                    isFilled = false;
                    break;
                }
            }
            if(isFilled)
            {
                for(int j = i; j > 0; j--)
                {
                    for(int k = 0; k < width; k++)
                        field[k][j] = field[k][j-1];
                }
                for(int j = 0; j < height; j++)
                    field[0][j] = -1;
                score = score + 1;
                parent.updateScore(score);
            }
        }
    }

    public void start()
    {
        isStarted = true;
        timer.start();
    }

    public void reset() //reset field what means that game can be started again
    {
        timer.stop();
        isFinished = false;
        isPaused = false;
        for(int i = 0; i < width; i++)
        {
            Arrays.fill(field[i], -1);
        }
        movingBrick = new Brick(5, 1);
        nextBrick = new Brick(5, 1);
        score = 0;
        parent.updateScore(score);
    }

    //list with colors of bricks, index in list is equal to to number of shape specific brick
    private Color[] colorList = new Color[] {Color.RED, Color.GRAY, Color.CYAN, Color.YELLOW, Color.PINK, Color.BLUE, Color.GREEN};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isStarted & !isFinished) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (field[i][j] >= 0) {
                        g.setColor(colorList[field[i][j]]);
                        g.fillRect(i * pixelSize, j * pixelSize, pixelSize, pixelSize);
                        g.setColor(Color.WHITE);
                        g.drawRect(i * pixelSize, j * pixelSize, pixelSize, pixelSize);
                    }
                }
            }
            int[][] temp = movingBrick.getElementsPosition();
            int shape = movingBrick.getShapeNumber();
            for (int i = 0; i < 4; i++) {
                g.setColor(colorList[shape]);
                g.fillRect(temp[i][0] * pixelSize, temp[i][1] * pixelSize, pixelSize, pixelSize);
                g.setColor(Color.WHITE);
                g.drawRect(temp[i][0] * pixelSize, temp[i][1] * pixelSize, pixelSize, pixelSize);
            }
        }
        if(isFinished) //shows GAME OVER when game is finished
        {
            g.setFont(new Font("Serif", Font.BOLD, pixelSize));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 2*pixelSize, 9*pixelSize);
        }
        Toolkit.getDefaultToolkit().sync();
    }
}

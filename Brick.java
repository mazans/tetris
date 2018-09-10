import java.util.Random;

public class Brick
{
    private static final int[][][] coordsTable = new int[][][]
            {
                    { {0, 1}, {0, 0}, {0, -1}, {0, -2} }, //I shape
                    { {0, 1}, {0, 0},{0, -1}, {1, -1} }, //L shape
                    { {0, 1}, {0, 0},{0, -1}, {-1, -1} }, //J shape
                    { {1, 1}, {0, 1}, {1,0}, {0, 0} }, //O shape
                    { {-1, 1}, {0, 1}, {1, 0}, {0, 0} }, //S shape
                    { {1, 1}, {0, 1}, {-1, 0}, {0, 0} }, //Z shape
                    { {-1, 1}, {0, 1}, {1, 1}, {0, 0} } //T shape
            }; //special coordinates which let program calculate real coords of moving brick

    private final int shape; //number which identifies shape of brick
    private int[][] coords = new int[4][2]; //table with coordinates from coordTable
    private int xPos;
    private int yPos;

    public Brick(int xPos, int yPos) //deifining brick's shape and setting their coords
    {
        Random rand = new Random();
        shape = rand.nextInt(7);
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 2; j++)
                coords[i][j] = coordsTable[shape][i][j];
        }
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int[][] getElementsPosition() //this method returns "real" coords of bricks in the field
    {
        int[][] position = new int[4][2];
        for(int i = 0; i < 4; i++)
        {
            position[i][0] = coords[i][0] + xPos;
            position[i][1] = -coords[i][1] + yPos;
        }
        return position;
    }

    public int getShapeNumber()
    {
        return shape;
    }

    public int getxPos()
    {
        return xPos;
    }

    public int getyPos()
    {
        return yPos;
    }

    public int[][] getCoords() //this method returns coords without including x and y position
    {
        int[][] position = new int[4][2];
        for(int i = 0; i < 4; i++)
        {
            position[i][0] = coords[i][0];
            position[i][1] = coords[i][1];
        }
        return position;
    }

    public void setCoords(int[][] newCoords)
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 2; j++)
            {
                coords[i][j] = newCoords[i][j];
            }
        }
    }

    public void setxPos(int xPos)
    {
        this.xPos = xPos;
    }

    public void setyPos(int yPos)
    {
        this.yPos =yPos;
    }

    public int[][] rotateLeft()
    {
        int[][] temp = new int[4][2];
        for(int i = 0; i < 4; i++)
        {
            temp[i][0] = -coords[i][1];
            temp[i][1] = coords[i][0];
        }
        return temp;
    }

    public int[][] rotateRight()
    {
        int[][] temp = new int[4][2];
        for(int i = 0; i < 4; i++)
        {
            temp[i][0] = coords[i][1];
            temp[i][1] = -coords[i][0];
        }
        return temp;
    }
}


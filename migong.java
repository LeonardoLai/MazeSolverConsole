import java.util.Scanner;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.lang.Exception;

public class migong {

    private static int startX, startY, endX, endY;
    private static final String wall = "\u001B[97m" + "X";   // "#"
    private static final String space = "\u001B[97m" + " ";  // "."
    private static final String start = "\u001B[96m" + "S";  // "S"
    private static final String end = "\u001B[96m" + "G";    // "G"
    private static final String path = "\u001B[32m" + "O";   // "+"
    private static final String deadEnd = "\u001B[33m" + "@";// "x"

    private static boolean solve(String map[][], int x, int y) throws Exception {
        // index out of range
        if (x >= map.length || x < 0 || y >= map[0].length || y < 0) {
            return false;
        }

        // base case: maze solved
        if (map[endX][endY].equals(path)) {
            for (int a = 0; a < map.length; a++) {
                for (int b = 0; b < map[0].length; b++) {
                    if (map[a][b].equals(deadEnd)) {
                        map[a][b] = space;
                    }
                }
            }
            // Set the starting and ending points back to its symbol.
            map[startX][startY] = start;
            map[endX][endY] = end;
            return true;
        }
    
        if (map[x][y].equals(space) || map[x][y].equals(start) || map[x][y].equals(end)) {
    
            map[x][y] = path;
            printMap(map, true);
    
            // it will follow "right, down, left, up" to search the first possible route.
            if (solve(map, x+1, y) || solve(map, x, y+1) || 
                solve(map, x-1, y) || solve(map, x, y-1)) {
                return true;
            } else {
                map[x][y] = deadEnd;
                return false;
            }
        } else {
            return false;
        }
    }

    private static void printMap(String[][] map, boolean wait) throws Exception {
        // using double for loops to print the map.
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                System.out.print(map[x][y]);
            }
            System.out.println();
        }
        if (wait) {
            TimeUnit.MILLISECONDS.sleep(250);
        }
        System.out.println();
    }

    // Convert the characters on the file to symbols for display and solving
    private static String convert(char input, int x, int y) {

        // Starting point
        if (input == 'S') {
            startX = x;
            startY = y;
            return start;

        // Ending point
        } else if (input == 'G') {
            endX = x;
            endY = y;
            return end;

        // Wall
        } else if (input == '#') {
            return wall;

        // for all other symbols, convert them to space.
        } else {
            return space;
        }
    }

    //    ========== THIS IS THE START OF THE MAIN METHOD ==========
    // throws InterruptedException for using TimeUnit.SECONDS.sleep(int).
    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        File mazeFile = null;
        Scanner fileReader = null;
        startX = startY = endX = endY = -1;

        // ===== Part One: Map Selection =====
        //print the dialog
        System.out.println("\u001B[36m" + "\nthere are 6 default maps, you may directly \ninput the number to select.\n");
        System.out.println("\u001B[96m" + "1. 6x6 tiny maze. \n2. 8x8 small maze. \n3. 10x10 regular maze. \n4. 15x15 large maze. \n5. 20x20 extra-large maze. \n6. 30x30 extreme maze.");

        // break this loop when file is successfully loaded.
        while (true) {

            // ask for selection
            System.out.print("\u001B[93m" + "\nyour selection (1-6)" + "\u001B[92m" + " > ");
            String selection = input.nextLine();
                
            //convert selection to real file name
            selection = "maze" + selection + ".txt";
            mazeFile = new File(selection);
            fileReader = new Scanner(mazeFile);
            break;
        }
        input.close();

        // ===== Part Two: Analysis and Import map ====
        int col = 0;
        int row = 0;

        // scan through the file to determin the size od array
        while (fileReader.hasNextLine()) {
            String currentLine = fileReader.nextLine();
            if (currentLine.length() > row) {
                row = currentLine.length();
            }
            col++;
        }
        String[][] map = new String[col][row];

        //reset the scanner and re-scan the file for input.
        fileReader.close();
        fileReader = new Scanner(mazeFile);
        for (int x = 0; x < col; x++) {
            String currentRow = fileReader.nextLine();
            for (int y = 0; y < row; y++) {
                // if the current row is shorter than the array size, fill Wall at end of row.
                if (y >= currentRow.length()) {
                    map[x][y] = wall;
                } else {
                    map[x][y] = convert(currentRow.charAt(y), x, y);
                }
            }
        }
        fileReader.close();

        // ===== Part Three: Solving the Maze =====
        System.out.println("\u001B[96m\nThis is the maze ... ");
        printMap(map, false);

        // check if there's starting and ending points on the map.
        if (startX == -1 && startY == -1) {
            System.out.println("\u001B[91m" + "...but there's no starting point on the map.\nThis maze cannot be solved.\u001B[0m");
            System.exit(3);
        } else if (endX == -1 || endY == -1) {
            System.out.println("\u001B[91m" + "...but there's no ending point on the map.\nThis maze cannot be solved.\u001B[0m");
            System.exit(4);
        }

        TimeUnit.MILLISECONDS.sleep(3000);
        // print success/fail message
        if (solve(map, startX, startY)) {
            System.out.println("\u001B[96m" + "... and this is the solution:");
            printMap(map, false);
        } else {
            System.out.println("\u001B[96m" + "\n... but there's no any path from the starting point to the ending point. " + "\u001B[91m" + "\nThis maze cannot be solved.\n");
            printMap(map, false);
        }
        // Reset the console color.
        System.out.print("\u001B[0m");
    }
}
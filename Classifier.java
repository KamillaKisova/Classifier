import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Classifier {

    private static final int STARTER_POINTS = 20;
    private static final int R = 0;
    private static final int G = 1;
    private static final int B = 2;
    private static final int P = 3;
    private static final int COLOUR = 0;
    private static final int TRUEC = 1;
    private static final int X = 2;
    private static final int Y = 3;
    private static final int MAX = 100;
    private static final double PROB = 0.01;
    private static int wrong = 0;

    static double distance(int[] pos1, int[] pos2){
        return Math.sqrt((pos2[Y] - pos1[Y])*(pos2[Y] - pos1[Y]) + (pos2[X] - pos1[X])*(pos2[X] - pos1[X]));
    }

    static int random(int start, int end){
        Random r = new Random();
        return r.nextInt(end - start) + start;
    }

    static void chooseColour(int[] pos, ArrayList<int[]> positions){
        int[] colours = {0,0,0,0};
        int count = 0, current = 0;
        for(int[] position : positions){
            colours[position[COLOUR]]++;
        }
        for(int i = 0; i < 4; i++){
            if(colours[i] > count){
                count = colours[i];
                current = i;
            }
        }
        pos[COLOUR] = current;
    }

    static void classify(int[] pos, int k, ArrayList<int[]> positions){
        positions.sort(new Comparator<int[]>() {
            @Override
            public int compare(int[] pos1, int[] pos2) {
                return Double.compare(distance(pos1, pos), distance(pos2, pos));
            }
        });

        ArrayList<int[]> kPositions = new ArrayList<>();
        for(int i = 0; i < k; i++){
            kPositions.add(positions.get(i));
        }

        chooseColour(pos, kPositions);
    }

    static ArrayList<int[]> kNN(ArrayList<int[]> positions, int k){
        ArrayList<int[]> allPos = new ArrayList<>(positions);
        Random r = new Random();
        int[] newPos;
        for(int i = 0; i < 40000; i++){
            newPos = new int[4];
            switch(i % 4){
                case R:
                    newPos[TRUEC] = R;
                    break;
                case G:
                    newPos[TRUEC] = G;
                    break;
                case B:
                    newPos[TRUEC] = B;
                    break;
                case P:
                    newPos[TRUEC] = P;
                    break;
            }

            if(r.nextDouble() < PROB){
                newPos[X] = random(-5000, 5000);
                newPos[Y] = random(-5000, 5000);
            }
            else{
                switch(i % 4){
                    case R:
                        newPos[X] = random(-5000, 500);
                        newPos[Y] = random(-5000, 500);
                        break;
                    case G:
                        newPos[X] = random(-500, 5000);
                        newPos[Y] = random(-5000, 500);
                        break;
                    case B:
                        newPos[X] = random(-5000, 500);
                        newPos[Y] = random(-500, 5000);
                        break;
                    case P:
                        newPos[X] = random(-500, 5000);
                        newPos[Y] = random(-500, 5000);
                        break;
                }
            }
            classify(newPos, k, positions);
            //System.out.println("X: "+newPos[X]+", Y: "+newPos[Y]+", TC: "+newPos[TRUEC]+", C: "+newPos[COLOUR]);
            allPos.add(newPos);
        }
        return allPos;
    }

    static void rateSuccess(ArrayList<int[]> positions){
        int all = positions.size() - STARTER_POINTS;
        int wrong = 0;
        for(int[] pos : positions){
            if(pos[COLOUR] != pos[TRUEC])
                wrong++;
        }
        System.out.println("Success rate: " + (1 - (float)wrong / (float)all) * 100 + "%");
    }

    static ArrayList<int[]> createList(String[] input){
        int colour, x, y;
        String[] singlePos;
        ArrayList<int[]> starterPositions = new ArrayList<>();
        int[] starterPos;
        for(int i = 0; i < STARTER_POINTS; i++){
            singlePos = input[i].split(" ");
            colour = Integer.parseInt(String.valueOf(singlePos[0]));
            x = Integer.parseInt(String.valueOf(singlePos[1]));
            y = Integer.parseInt(String.valueOf(singlePos[2]));
            starterPos = new int[4];
            starterPos[COLOUR] = colour;
            starterPos[TRUEC] = colour;
            starterPos[X] = x;
            starterPos[Y] = y;
            starterPositions.add(starterPos);
        }
        return starterPositions;
    }

    static ArrayList<int[]> readInput() {
        BufferedReader reader = null;
        String path = "input/input.txt";
        File file = new File(path);
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (FileNotFoundException f){
            System.out.println("Input file is does not exist.");
            System.exit(0);
        }
        String inputLine;
        String[] inputWhole = new String[STARTER_POINTS];
        try {
            for(int i = 0; i < STARTER_POINTS; i++){
                inputLine = reader.readLine();
                inputWhole[i] = inputLine;
            }
        } catch(IOException io){
            System.out.println("IOException");
            System.exit(0);
        }
        return createList(inputWhole);
    }

    public static void main(String[] args){
        int k = 0, valid = 0;
        Scanner scan = new Scanner(System.in);
        ArrayList<int[]> positions = readInput();
        ArrayList<int[]> allPos;
        long start, end;
        Frame f = null;

        while(true){
            valid = 0;
            while(valid == 0){
                System.out.println("Please enter the value of K:");
                System.out.println("1 - 3 - 7 - 15 - neighbours");
                System.out.println("0 - exit");
                k = scan.nextInt();
                if(k == 1 || k == 3 || k == 7 || k == 15){
                    valid = 1;
                    break;
                }
                else if(k == 0){
                    return;
                }
                else
                    System.out.println("Please choose from the listed numbers.");
                if(f != null){
                    f.dispose();
                }
            }
//            classify(newPos, k, positions);
//            for(int[] pos : positions){
//                System.out.println("X: " + pos[X] + ", Y: " + pos[Y] + ", C: " + pos[COLOUR]);
//            }


            start = System.currentTimeMillis();
            allPos = kNN(positions, k);
            end = System.currentTimeMillis() - start;
            System.out.println("Time spent searching: " + (float)end/1000 + " sec");
            rateSuccess(allPos);
//            for(int[] pos : allPos){
//                System.out.println("X: " + pos[X] + ", Y: " + pos[Y] + ", C: " + pos[COLOUR]);
//            }

            f = new MyFrame(allPos);
        }
    }
}

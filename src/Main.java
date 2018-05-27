import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// Name : Bara Adnan
// BZU #ID : 1161357

public class Main {

    public static void main(String[] args)
    {
        // The matrices needed
        ArrayList<Integer> PR = new ArrayList(), max = new ArrayList(), alloc = new ArrayList(), needs = new ArrayList(), avail = new ArrayList();
        String filePath = "Data.txt";

        // Reading the file and saving the data
        readFile(filePath, PR, max, alloc, avail);
        int processes = Integer.parseInt(PR.get(0).toString());
        int resources = Integer.parseInt(PR.get(1).toString());

        // Setting up the "K" boolean array, and the sequence
        boolean[] finish = new boolean[processes];
        int[] sequence = new int[processes];

        // Initializing them
        for(int i = 0; i < processes; i++)
        {
            finish[i] = false;
            sequence[i] = -1;
        }

        // Calculating the needs matrix
        for(int i = 0; i < processes * resources; i++)
        {
            needs.add(Integer.parseInt(String.valueOf(max.get(i))) - Integer.parseInt(String.valueOf(alloc.get(i))));
        }

        // Printing the required data
        printInfo(PR, max, alloc, needs, avail);

        // Solving the problem
        int j = 0;
        while(j < processes)
        {
            // Finding the next suitable process to give resources to
            int nextProcess = findSuitable(processes, resources, finish, needs, avail);

            // if -1, that means no more suitable processes can be found
            if(nextProcess == -1)
                break;

            // Summation of the updated available array
            for(int i = nextProcess * resources, k = 0; i < (nextProcess * resources) + resources; i++, k++)
                avail.set(k, Integer.parseInt(String.valueOf(avail.get(k))) + Integer.parseInt(String.valueOf(alloc.get(i))));

            // Updating the finish boolean array, and the safe sequence array
            finish[nextProcess] = true;
            sequence[j] = nextProcess;
            j++;
        }

        // Printing an error if the system isn't safe
        if(!isFinished(finish))
            System.out.println("ERROR!! System is not safe!");
        // Printing success message, and the safe sequence if the system is safe
        else
        {
            System.out.println("System is safe!\n");

            System.out.print("The safe sequence is: <");

            for (int i = 0; i < sequence.length - 1; i++)
                System.out.print("P" + sequence[i] + " . ");
            System.out.println("P" + sequence[sequence.length - 1] + ">\n");
        }
    }

    // A method that prints the data on the terminal
    public static void printInfo(ArrayList PR, ArrayList maz, ArrayList alloc, ArrayList needs, ArrayList avail)
    {
        System.out.println("\nNumber of processes running = " + PR.get(0));
        System.out.println("Number of resource types = " + PR.get(1));

        System.out.println("\nProcess\t\tMaximum\t\tAllocation\t\tCurrent Needs");

        for(int i = 0; i < Integer.parseInt(PR.get(0).toString()); i++)
        {
            System.out.print("P" + i + "\t\t\t");

            for (int j = i * Integer.parseInt(PR.get(1).toString()); j < (i * Integer.parseInt(PR.get(1).toString())) + Integer.parseInt(PR.get(1).toString()); j++)
                System.out.print(maz.get(j) + " ");
            System.out.print("\t\t");

            for (int j = i * Integer.parseInt(PR.get(1).toString()); j < (i * Integer.parseInt(PR.get(1).toString())) + Integer.parseInt(PR.get(1).toString()); j++)
                System.out.print(alloc.get(j) + " ");
            System.out.print("\t\t\t");

            for (int j = i * Integer.parseInt(PR.get(1).toString()); j < (i * Integer.parseInt(PR.get(1).toString())) + Integer.parseInt(PR.get(1).toString()); j++)
                System.out.print(needs.get(j) + " ");

            System.out.println();
        }

        System.out.print("\nAvailable: ");
        for (int i = 0; i < Integer.parseInt(PR.get(1).toString()); i++)
            System.out.print(avail.get(i) + " ");

        System.out.println("\n\n--------------------------------------------\n");
    }

    // Checks if all processes have been allocated resources to and finished
    public static boolean isFinished(boolean[] finish)
    {
        for(int i = 0; i < finish.length; i++)
            if(!finish[i])
                return false;

        return true;
    }

    // Finds the next suitable process to give resources to
    public static int findSuitable(int processes, int resources, boolean[] finish, ArrayList needs, ArrayList avail)
    {
        for(int i = 0; i < processes; i++)
        {
            // If it's already done, then it's not suitable
            if(finish[i])
                continue;

            for(int j = i * resources, k = 0; j < (i * resources) + resources; j++, k++)
            {
                if(Integer.parseInt(needs.get(j).toString()) > Integer.parseInt(avail.get(k).toString()))
                    break;

                if(j == (i * resources) + resources - 1)
                    return i;
            }
        }

        return -1;
    }

    // A method that reads the file and stores the data
    public static void readFile(String filePath, ArrayList PR, ArrayList max, ArrayList alloc, ArrayList avail)
    {
        File file = new File(filePath);
        Scanner input = null;
        String str = null;

        try
        {
            input = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return;
        }

        for(int i = 0; i < 2; i++)
        {
            str = input.nextLine();
            PR.add(Integer.parseInt(str.split("= ")[1]));
        }

        input.nextLine();
        input.nextLine();

        for(int i = 0; i < Integer.parseInt(PR.get(0).toString()); i++)
        {
            str = input.nextLine();
            String[] inputs = str.split("\t\t");

            String[] maxs = inputs[0].split(" ");
            String[] allocs = inputs[1].split(" ");

            for(int j = 0; j < Integer.parseInt(PR.get(1).toString()); j++)
            {
                max.add(maxs[j]);
                alloc.add(allocs[j]);
            }
        }

        input.nextLine();
        input.nextLine();

        str = input.nextLine();
        String[] avails = str.split(" ");

        for(int i = 0; i < Integer.parseInt(PR.get(1).toString()); i++)
            avail.add(avails[i]);
    }
}

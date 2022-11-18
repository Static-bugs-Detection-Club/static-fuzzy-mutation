package buildtree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class SelectMutation {
    public static ArrayList<Integer> getWeight(int[] weight, int number) {
        ArrayList<Integer> result = new ArrayList<>(number);
        int weightSum = 0;
        for (int wc : weight) {
            weightSum += wc;
        }

        if (number <= 0) {
            System.err.println("Error: number=" + number);
            return result;
        }
        for (int i = 0; i < number; i++) {
            Random random = new Random();
            int n = random.nextInt(weightSum); // n in [0, weightSum)
            int m = 0;
            for (int j = 0; j < weight.length; j++) {
                if (m <= n && n < m + weight[j]) {
                    result.add(j);
                    break;
                }
                m += weight[j];
            }
        }
        return result;
    }


    public static LinkedList<Mutation> selectMutation(ArrayList<Integer> arrayList){
        LinkedList<Mutation> mutations = new LinkedList<>();
        for (int i: arrayList) {
            if(i==0)
                mutations.add(new ToGlobal());
            else if(i==1)
                mutations.add(new AddLock());
            else if(i==2)
                mutations.add(new AddSignal());
            else if(i==3)
                mutations.add(new ReplaceJoin());
            else if(i==4)
                mutations.add(new ToEqual());
            else if(i==5)
                mutations.add(new ToGreater());
            else if(i==6)
                mutations.add(new ToSubtract());
            else if(i==7)
                mutations.add(new ToUnEqual());
            else if(i==8)
                mutations.add(new ToDoublePlusOrDe());
        }
        return mutations;
    }
}

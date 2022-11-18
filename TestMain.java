

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String s = scanner.next();
        char[] chars = s.toCharArray();
        int re = 0;
        for (int i = 0; i < n-2; i++) {
            HashMap<Character, Integer> hashMap = new HashMap<>();
            for (int j = i+1; j < n; j++) {
                if(chars[i] != chars[j]){
                    if(!hashMap.containsKey(chars[j])){
                        hashMap.put(chars[j], 1);
                    }else {
                        hashMap.put(chars[j], hashMap.get(chars[j]) + 1);
                    }
                }
            }
            for (Character key: hashMap.keySet()) {
                int num = hashMap.get(key);
                int res = 0;
                if(num >= 2){
                    res = combine(num, 2, res);
                    int temp = res;
                    re += temp;
                }
            }
        }
        System.out.println(re);

    }


    public static int combine(int n, int k, int res) {
        if (n <= 0 || k <= 0 || k > n) {
            return res;
        }

        ArrayList<Integer> c = new ArrayList<>();
        generateCombinations(n, k, 1, c, res);
        return res;

    }

    private static void generateCombinations(int n, int k, int start, ArrayList<Integer> c, int res) {
        if (c.size() == k) {
            //这里需要注意java的值传递
            //此处必须使用重新创建对象的形式，否则 res 列表中存放的都是同一个引用
            res++;
            return;
        }

        //通过终止条件，进行剪枝优化，避免无效的递归
        //c中还剩 k - c.size()个空位，所以[ i ... n]中至少要有k-c.size()个元素
        //所以i最多为 n - (k - c.size()) + 1
        for(int i = start;i <= n - (k - c.size()) + 1; i++) {
            c.add(i);
            generateCombinations(n, k, i + 1, c,res);
            //记得回溯状态啊
            c.remove(c.size() - 1);
        }
    }


}

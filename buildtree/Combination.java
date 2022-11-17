package buildtree;

import java.util.ArrayList;

public class Combination {
    private static ArrayList<ArrayList<Integer>> res = new ArrayList<>();

    public static ArrayList<ArrayList<Integer>> getCombine(int n){
        res.clear();
        for(int i = 1; i <= n; i++) {
            combine(n, i);
        }
        return res;
    }

    public static void combine(int n, int k) {
        if (n <= 0 || k <= 0 || k > n) {
            return;
        }

        ArrayList<Integer> c = new ArrayList<>();
        generateCombinations(n, k, 1, c);

    }

    /**
     * 回溯求所有组合结果
     * @param n
     * @param k
     * @param start 开始搜索新元素的位置
     * @param c 当前已经找到的组合
     */
    private static void generateCombinations(int n, int k, int start, ArrayList<Integer> c) {
        if (c.size() == k) {
            //这里需要注意java的值传递
            //此处必须使用重新创建对象的形式，否则 res 列表中存放的都是同一个引用
            res.add(new ArrayList<>(c));
            return;
        }

        //通过终止条件，进行剪枝优化，避免无效的递归
        //c中还剩 k - c.size()个空位，所以[ i ... n]中至少要有k-c.size()个元素
        //所以i最多为 n - (k - c.size()) + 1
        for(int i = start;i <= n - (k - c.size()) + 1; i++) {
            c.add(i);
            generateCombinations(n, k, i + 1, c);
            //记得回溯状态啊
            c.remove(c.size() - 1);
        }
    }
}

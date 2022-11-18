import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws CoreException, IOException {
        IASTTranslationUnit translationUnit = GetSubtree.getAST("C:\\Users\\eclipse\\Desktop\\数据\\变异\\102225bad.txt");
        ArrayList<ArrayList<String>> lists = GetSubtree.getTree(translationUnit);
        //System.out.println(lists);
        ArrayList<String> list = GetSubtree.getMainName(translationUnit);
        SecondMutation.genMainFun(list);
        ArrayList<ArrayList<String>> secondList = SecondMutation.getTree("C:\\Users\\eclipse\\Desktop\\数据\\变异\\second\\");
//        ThirdMutation.genMainFun(list);
//        ArrayList<ArrayList<String>> thirdList = ThirdMutation.getTree("C:\\Users\\eclipse\\Desktop\\数据\\变异\\third\\");
        int n = lists.size();


        for(int i = 1; i <= n; i++) {
            combine(n, i);
        }

        int m = res.size();
        String string1 = "102225badFirstMutation";
        String string2 = "secondMutation";
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < secondList.size(); j++)
            FirstMutation.getAst(translationUnit, lists, res.get(i), string1 + i + string2 + j, secondList.get(j));
        }
//        for(int i = 0; i < m; i++) {
//            for(int j = 0; j < secondList.size(); j++)
//                FirstMutation.getAst(translationUnit, lists, res.get(i), string1 + i + string2 + j + "ThirdMutation0", thirdList.get(j));
//        }

    }

    private static ArrayList<ArrayList<Integer>> res = new ArrayList<>();

    public static ArrayList<ArrayList<Integer>> combine(int n, int k) {
        if (n <= 0 || k <= 0 || k > n) {
            return res;
        }

        ArrayList<Integer> c = new ArrayList<>();
        generateCombinations(n, k, 1, c);
        return res;

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

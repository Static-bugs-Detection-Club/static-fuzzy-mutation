package buildtree;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.internal.core.model.TranslationUnit;
import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class AddThread {
    public static ArrayList<Node> main(String path, String str) throws CoreException, IOException {
        Node node = BuildTree.getTree(path);
        IASTTranslationUnit translationUnit =  BuildTree.getAST(path);
        ArrayList<String> strings = BuildTree.getMainName(translationUnit);
        String string = "D:\\论文资料\\变异\\";
        File file = new File("D:\\论文资料\\变异\\" + str + "\\");
        if(!file.exists())
            file.mkdirs();
        genMainFun(strings, string + str + "\\");
        ArrayList<Node> nodes = getFileName(string + str + "\\");
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        for (Node child: nodes) {
            Node pNode = SerializationUtils.clone(node);
            Node node1 = child.getSonList().get(0);
            pNode.getSonList().add(node1);
            nodeArrayList.add(pNode);
        }
        System.out.println(nodeArrayList.size());
        return  nodeArrayList;
    }

    private static boolean fileLog = true;

    public static OutputStream getOutputStream(String str) throws IOException {
        if (fileLog) {

            File file = new File(str + ".txt");
            if (file.exists()){
                file.delete();
                file.createNewFile();
            }else {
                file.createNewFile();
            }
            return new FileOutputStream(file, true);
        } else {
            return System.out;
        }
    }

    public static void log(String info, String str) throws IOException {
        OutputStream out = getOutputStream(str);
        out.write(info.getBytes("utf-8"));
        out.write("\r\n".getBytes());
    }

    public static void genMainFun(ArrayList<String> lists, String str) throws IOException {

        int n = lists.size();
        for(int i = 1; i <= n; i++){
            combine(n, i);
        }

        for (int i = 0; i < res.size(); i++){
            String string = str + "addThread" +i;
            log("int main(){", string);
            ArrayList<Integer> list = res.get(i);
            int threadCount = 1;
            for(int j = 1; j <= list.size(); j++){
                log("    pthread_t th" + threadCount +";",string);
                threadCount++;
                if (list.size() == 1){
                    log("    pthread_t th" + threadCount +";",string);
                }
            }
            for(int j = 1; j <= list.size(); j++){
                log("    pthread_create(&th"+j+", null, "+lists.get(list.get(j-1)-1)+", NULL);", string);
                if(list.size() == 1)
                    log("    pthread_create(&th"+ threadCount +", null, "+lists.get(list.get(j-1)-1)+", NULL);", string);
            }
            for(int j = 1; j <= list.size(); j++){
                log("    pthread_join(&th" + j +", NULL);",string);
                if(list.size() == 1)
                    log("    pthread_join(&th" + threadCount +", NULL);",string);
            }
            log("    return 0;",string);
            log("}", string);
        }
        res.clear();
        //pthread_join(&th"<<pthreadCount<<",NULL);
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

    public static ArrayList<Node> getFileName(String path) throws IOException, CoreException {

        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return null;

        }
        ArrayList<Node> nodes = new ArrayList<>();

        File[] fa = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                getFileName(path + fs.getName()+"\\");
            } else {
                Node node = BuildTree.getTree(path + fs.getName());
                nodes.add(node);
            }

        }
        return nodes;
    }
}

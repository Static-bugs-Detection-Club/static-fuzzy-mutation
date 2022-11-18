import org.eclipse.cdt.core.dom.ast.*;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThirdMutation {
    static ArrayList<ArrayList<String>> lists = new ArrayList<>();

    public static ArrayList<ArrayList<String>> getTree(String path) throws CoreException {
        Map<String, String> map = getFilesDatas(path);
        for (String key: map.keySet()) {
            FileContent fileContent = FileContent.createForExternalFileLocation(path + key);

            Map definedSymbols = new HashMap();
            String[] includePaths = new String[0];
            IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
            IParserLogService log = new DefaultLogService();

            IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

            int opts = 8;
            IASTTranslationUnit translationUnit = GCCLanguage.getDefault().getASTTranslationUnit(fileContent, info,
                    emptyIncludes, null, opts, log);

            IASTPreprocessorIncludeStatement[] includes = translationUnit.getIncludeDirectives();
            for (IASTPreprocessorIncludeStatement include : includes) {
                System.out.println("include - " + include.getName());
            }

            ArrayList<String> list = new ArrayList<>();
            printTree(translationUnit,1, list);
            lists.add(list);
        }
        return lists;
    }

    public static ArrayList<IASTNode> getTreeUnit(String path) throws CoreException {
        Map<String, String> map = getFilesDatas(path);
        ArrayList<IASTNode> nodes = new ArrayList<>();
        for (String key: map.keySet()) {
            FileContent fileContent = FileContent.createForExternalFileLocation(path + key);

            Map definedSymbols = new HashMap();
            String[] includePaths = new String[0];
            IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
            IParserLogService log = new DefaultLogService();

            IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

            int opts = 8;
            IASTTranslationUnit translationUnit = GCCLanguage.getDefault().getASTTranslationUnit(fileContent, info,
                    emptyIncludes, null, opts, log);
            nodes.add(translationUnit);

        }
        return nodes;
    }

    public static void printTree(IASTNode node, int index, ArrayList<String> list) {
        IASTNode[] children = node.getChildren();

        boolean printContents = true;

        if ((node instanceof CASTTranslationUnit)) {
            printContents = false;
        }

        String offset = "";
        try {
            offset = node.getSyntax() != null ? " (offset: " + node.getFileLocation().getNodeOffset() + "," + node.getFileLocation().getNodeLength() + ")" : "";
            printContents = node.getFileLocation().getNodeLength() < 30;
        } catch (ExpansionOverlapsBoundaryException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            offset = "UnsupportedOperationException";
        }

        if (node.getClass().getSimpleName().equals("CASTTranslationUnit")) {

        } else if(node.getClass().getSimpleName().equals("CASTCompoundStatement")){

            list.add(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    node.getClass().getSimpleName() + "  " +
                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                            node.getRawSignature().subSequence(0, 1)));
        }else {
            list.add(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    node.getClass().getSimpleName() + "  " +
                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                            node.getRawSignature().subSequence(0, 5)));
        }

        for (IASTNode iastNode : children)
            printTree(iastNode, index + 1, list);
    }

    private static boolean fileLog = true;
    private static String logFileName = "C:\\Users\\eclipse\\Desktop\\数据\\变异\\ASTResut\\";// 指定程序执行结果保存的文件路径

    public static OutputStream getOutputStream(String str) throws IOException {
        if (fileLog) {
            File file = new File("C:\\Users\\eclipse\\Desktop\\数据\\变异\\Third\\" + str+".txt");
            if (!file.exists())
                file.createNewFile();
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

    public static void genMainFun(ArrayList<String> lists) throws IOException {
        String str = "SecondMutation";
        int n = lists.size();
        for(int i = 1; i <= n; i++){
            combine(n, i);
        }

        for (int i = 0; i < res.size(); i++){
            String string = str + i + "ThirdMutation0";
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
                log("    " + lists.get(list.get(j-1)-1) + "();",string);
                if(list.size() == 1)
                    log("    " + lists.get(list.get(j-1)-1) + "();",string);
            }
            log("    return 0;",string);
            log("}", string);
        }

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


    public static Map<String, String> getFilesDatas(String filePath) {
        Map<String, String> files = new HashMap<>();
        File file = new File(filePath); // 需要获取的文件的路径
        String[] fileNameLists = file.list(); // 存储文件名的String数组
        File[] filePathLists = file.listFiles(); // 存储文件路径的String数组
        for (int i = 0; i < filePathLists.length; i++) {
            if (filePathLists[i].isFile()) {
                try {// 读取指定文件路径下的文件内容
                    String fileDatas = readFile(filePathLists[i]);
                    // 把文件名作为key,文件内容为value 存储在map中
                    files.put(fileNameLists[i], fileDatas);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return files;
    }

    public static String readFile(File path) throws IOException {
        // 创建一个输入流对象
        InputStream is = new FileInputStream(path);
        // 定义一个缓冲区
        byte[] bytes = new byte[1024];// 1kb
        // 通过输入流使用read方法读取数据
        int len = is.read(bytes);
        // System.out.println("字节数:"+len);
        String str = null;
        while (len != -1) {
            // 把数据转换为字符串
            str = new String(bytes, 0, len);
            // System.out.println(str);
            // 继续进行读取
            len = is.read(bytes);
        }
        // 释放资源
        is.close();
        return str;
    }
}


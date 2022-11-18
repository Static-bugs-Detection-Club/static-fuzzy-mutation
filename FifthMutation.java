import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FifthMutation {
    public static void getFifthMutation(String path, String filename) throws CoreException, IOException {
        String path3 = path;
        String path1 = "D:\\eclipseproject\\新建文件夹\\eclipse-cdt-standalone-astparser-master\\src\\main\\tempfile\\Semaphore.txt";
        String path2 = "D:\\eclipseproject\\新建文件夹\\eclipse-cdt-standalone-astparser-master\\src\\main\\tempfile\\sem.txt";
        IASTTranslationUnit translationUnit1 = getAST(path1);
        IASTTranslationUnit translationUnit2 = getAST(path2);

        ArrayList<IASTNode> semaphoreNodeList = new ArrayList<>();
        ArrayList<IASTNode> semNodeList = new ArrayList<>();
        IASTNode[] nodes = translationUnit2.getChildren();
        IASTNode funNode = nodes[0];
        for (IASTNode n : funNode.getChildren()) {
            if (n.getClass().getSimpleName().equals("CASTCompoundStatement")) {
                IASTNode[] nodes1 = n.getChildren();
                for (IASTNode n1 : nodes1) {
                    semNodeList.add(n1);
                }
            }
        }
        IASTNode[] iastNodes = translationUnit1.getChildren();
        semaphoreNodeList.add(iastNodes[0]);


        ArrayList<ArrayList<IASTNode>> statementNodeLists = new ArrayList<>();
        ArrayList<IASTNode> funNodeLists = new ArrayList<>();
        IASTTranslationUnit translationUnit3 = getAST(path3);
        IASTNode node = translationUnit3;
        IASTNode[] children = node.getChildren();
        for (IASTNode node1 : children) {
            if (node1.getClass().getSimpleName().equals("CASTFunctionDefinition")) {
                funNodeLists.add(node1);
                IASTNode[] child = node1.getChildren();
                for (IASTNode node2 : child) {
                    if (node2.getClass().getSimpleName().equals("CASTCompoundStatement")) {
                        addNode(node2, statementNodeLists);
                    }
                }
            }
        }

        for (int i = 0; i < statementNodeLists.size(); i++) {
            ArrayList<IASTNode> stateNode = statementNodeLists.get(i);
            for (int j = 0; j < stateNode.size(); j++) {
                if(stateNode.get(j).getRawSignature().indexOf("free(") >= 0){
                    stateNode.add(j+1, semNodeList.get(0));
                }
                System.out.println(stateNode.get(j).getRawSignature());
            }
        }
        int tempNum = 0;

        for (int i = 0; i < statementNodeLists.size(); i++) {
            ArrayList<IASTNode> stateNode = statementNodeLists.get(i);
            int n = stateNode.size();
            for (int j = 0; j < n; j++) {
                String fileName = filename.substring(0, filename.indexOf(".")) + "fifthMutation" + tempNum;
                printAST(node, fileName, j, 1, funNodeLists.get(i), semNodeList, stateNode, semaphoreNodeList);
                tempNum++;
            }
        }
    }


    //存储语句子树
    public static void addNode(IASTNode node, ArrayList<ArrayList<IASTNode>> lists) {
        IASTNode[] child = node.getChildren();
        ArrayList<IASTNode> list = new ArrayList<>();
        for (IASTNode n : child) {
            if (n.getClass().getSimpleName().indexOf("Statement") > 0)
                list.add(n);
        }
        lists.add(list);
    }

    public static void log(String info, String str) throws IOException {
        OutputStream out = getOutputStream(str);
        out.write(info.getBytes("utf-8"));
        out.write("\r\n".getBytes());
    }

    private static boolean fileLog = true;

    public static OutputStream getOutputStream(String str) throws IOException {
        if (fileLog) {
            File file = new File("D:\\论文资料\\变异\\fifth\\" + str + ".txt");
            if (!file.exists())
                file.createNewFile();
            return new FileOutputStream(file, true);
        } else {
            return System.out;
        }
    }

    //从根结点开始打印
    public static void printAST(IASTNode rootNode, String path, int k, int index, IASTNode funNode, ArrayList<IASTNode> semNodeList, ArrayList<IASTNode> stateLists,ArrayList<IASTNode> semaphoreNodeList) throws IOException {
        IASTNode[] child = rootNode.getChildren();
        boolean printContents = true;

        if ((rootNode instanceof CASTTranslationUnit)) {
            printContents = false;
        }
        if (rootNode.equals(funNode)) {
            printCommon(funNode, path, k, index, semNodeList, stateLists);
            return;
        }
        if (rootNode.getClass().getSimpleName().equals("CASTCompoundStatement")) {
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    rootNode.getClass().getSimpleName() + "  " +
                    (printContents ? rootNode.getRawSignature().replaceAll("\n", " \\ ") :
                            rootNode.getRawSignature().subSequence(0, 1)), path);
        } else if(rootNode.getClass().getSimpleName().equals("CASTTranslationUnit")){
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    rootNode.getClass().getSimpleName() + "  " +
                    (printContents ? rootNode.getRawSignature().replaceAll("\n", " \\ ") :
                            rootNode.getRawSignature().subSequence(0, 5)), path);
            printStateTree(semaphoreNodeList.get(0), index+1, path);
        }else {
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    rootNode.getClass().getSimpleName() + "  " +
                    (printContents ? rootNode.getRawSignature().replaceAll("\n", " \\ ") :
                            rootNode.getRawSignature().subSequence(0, 5)), path);
        }

        for (IASTNode node : child) {
            printAST(node, path, k, index + 1, funNode, semNodeList, stateLists, semaphoreNodeList);
        }
    }

    //打印普通子树
    public static void printCommon(IASTNode node, String path, int j, int index, ArrayList<IASTNode> semNodeList, ArrayList<IASTNode> stateLists) throws IOException {
        IASTNode[] child = node.getChildren();
        boolean printContents = true;

        if ((node instanceof CASTTranslationUnit)) {
            printContents = false;
        }
        if (node.getClass().getSimpleName().equals("CASTCompoundStatement")) {
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    node.getClass().getSimpleName() + "  " +
                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                            node.getRawSignature().subSequence(0, 1)), path);
            printSubTree( j, index + 1, semNodeList, stateLists, path);
            return;
        } else {
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    node.getClass().getSimpleName() + "  " +
                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                            node.getRawSignature().subSequence(0, 5)), path);
        }

        for (IASTNode iastNode : child) {
            printCommon(iastNode, path, j, index + 1, semNodeList, stateLists);
        }
    }

    //打印函数内部结点
    public static void printSubTree( int j, int index, ArrayList<IASTNode> semNodeList, ArrayList<IASTNode> stateLists, String path) throws IOException {
        for (int i = 0; i < stateLists.size(); i++) {
            if (i == j && !(stateLists.get(i).getRawSignature().indexOf("sem.signal()") >= 0)) {
                printStateTree(semNodeList.get(1), index, path);
            }
            printStateTree(stateLists.get(i), index, path);

        }
    }

    //打印内部语句节点子树
    public static void printStateTree(IASTNode node, int index, String path) throws IOException {
        IASTNode[] child = node.getChildren();
        boolean printContents = true;

        if ((node instanceof CASTTranslationUnit)) {
            printContents = false;
        }

        log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                node.getClass().getSimpleName() + "  " +
                (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                        node.getRawSignature().subSequence(0, 5)), path);


        for (IASTNode iastNode : child) {
            printStateTree(iastNode, index + 1, path);
        }
    }


    public static IASTTranslationUnit getAST(String path) throws CoreException {
        FileContent fileContent = FileContent.createForExternalFileLocation(path);

        Map definedSymbols = new HashMap();
        String[] includePaths = new String[0];
        IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
        IParserLogService log = new DefaultLogService();

        IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

        int opts = 8;
        IASTTranslationUnit translationUnit = GCCLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);

        return translationUnit;
    }
}

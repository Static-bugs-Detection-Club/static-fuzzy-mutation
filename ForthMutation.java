import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.*;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTVisibilityLabel;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;

public class ForthMutation {
    public static void noFourMutation(String path, String filename) throws CoreException, IOException {
        String path1 = path;
        String path2 = "C:\\Users\\eclipse\\Desktop\\数据\\变异\\pthreadLock\\main.txt";
        IASTTranslationUnit translationUnit2 = getAST(path2);
        ArrayList<IASTNode> lockLists = new ArrayList<>();
        //getTree(translationUnit2, lists1);
        //getTree(translationUnit1, lists2);
        IASTNode[] nodes = translationUnit2.getChildren();
        IASTNode funNode = nodes[0];
        for (IASTNode n: funNode.getChildren()) {
            if(n.getClass().getSimpleName().equals("CASTCompoundStatement")){
                IASTNode[] nodes1 = n.getChildren();
                for (IASTNode n1: nodes1) {
                    lockLists.add(n1);
                }
            }
        }

        ArrayList<ArrayList<IASTNode>> statementNodeLists = new ArrayList<>();
        ArrayList<IASTNode> funNodeLists = new ArrayList<>();
        IASTTranslationUnit translationUnit1 = getAST(path1);
        IASTNode node = translationUnit1;
        IASTNode[] children = node.getChildren();
        for (IASTNode node1: children) {
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
        int tempNum = 0;
        for (int i = 0; i < statementNodeLists.size(); i++) {
            ArrayList<IASTNode> stateNode = statementNodeLists.get(i);
            int n = stateNode.size();
            for (int j = 0; j < n; j++) {
                for (int k = j; k < n; k++) {
                    String fileName = filename+ "forthMutation" + tempNum;
                    printAST(node, fileName,j, k, 1, funNodeLists.get(i), lockLists, stateNode);
                    tempNum++;
                }
            }
        }
    }
    public static void addNode(IASTNode node, ArrayList<ArrayList<IASTNode>> lists){
        IASTNode[] child = node.getChildren();
        ArrayList<IASTNode> list = new ArrayList<>();
        for (IASTNode n: child) {
            if(n.getClass().getSimpleName().indexOf("Statement") > 0)
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
            File file = new File("D:\\论文资料\\变异\\forth\\" + str+".txt");
            if (!file.exists())
                file.createNewFile();
            return new FileOutputStream(file, true);
        } else {
            return System.out;
        }
    }

    //从根结点开始打印
    public static void printAST(IASTNode rootNode, String path, int j, int k, int index, IASTNode funNode, ArrayList<IASTNode> lockLists, ArrayList<IASTNode> stateLists) throws IOException {
        IASTNode[] child = rootNode.getChildren();
        boolean printContents = true;

        if ((rootNode instanceof CASTTranslationUnit)) {
            printContents = false;
        }
        if(rootNode.equals(funNode)){
            printCommon(funNode, path, j, k, index, lockLists, stateLists);
            return;
        }
        if(rootNode.getClass().getSimpleName().equals("CASTCompoundStatement")){
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    rootNode.getClass().getSimpleName() + "  " +
                    (printContents ? rootNode.getRawSignature().replaceAll("\n", " \\ ") :
                            rootNode.getRawSignature().subSequence(0, 1)),path);
        }else {
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "})+
                    rootNode.getClass().getSimpleName() + "  " +
                    (printContents ? rootNode.getRawSignature().replaceAll("\n", " \\ ") :
                            rootNode.getRawSignature().subSequence(0, 5)),path);
        }

        for (IASTNode node: child) {
            printAST(node, path, j, k, index + 1, funNode, lockLists, stateLists);
        }
    }

    //打印普通子树
    public static void printCommon(IASTNode node, String path, int j, int k, int index, ArrayList<IASTNode> lockLists, ArrayList<IASTNode> stateLists) throws IOException {
        IASTNode[] child = node.getChildren();
        boolean printContents = true;

        if ((node instanceof CASTTranslationUnit)) {
            printContents = false;
        }
        if(node.getClass().getSimpleName().equals("CASTCompoundStatement")){
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    node.getClass().getSimpleName() + "  " +
                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                            node.getRawSignature().subSequence(0, 1)),path);
            printSubTree(j, k, index + 1, lockLists, stateLists, path);
            return;
        }else {
            log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                    node.getClass().getSimpleName() + "  " +
                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                            node.getRawSignature().subSequence(0, 5)),path);
        }

        for (IASTNode iastNode: child) {
            printCommon(iastNode, path, j, k, index + 1, lockLists, stateLists);
        }
    }

    //打印函数内部结点
    public static void printSubTree(int j, int k, int index, ArrayList<IASTNode> lockLists, ArrayList<IASTNode> stateLists, String path) throws IOException {
        for (int i = 0; i < stateLists.size(); i++) {
            if(i == j){
                printStateTree(lockLists.get(0), index, path);
            }
            printStateTree(stateLists.get(i), index, path);
            if(i == k){
                printStateTree(lockLists.get(1),index, path);
            }
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
                            node.getRawSignature().subSequence(0, 5)),path);


        for (IASTNode iastNode: child) {
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

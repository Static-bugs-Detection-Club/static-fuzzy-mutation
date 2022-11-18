import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
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

public class NormalMutationOne {
    public static void getNormalOne(String path, String fileName) throws CoreException, IOException {
        IASTTranslationUnit translationUnit1 = getAST(path);


        ArrayList<ArrayList<IASTNode>> statementNodeLists = new ArrayList<>();
        ArrayList<IASTNode> funNodeLists = new ArrayList<>();
        IASTNode[] children = translationUnit1.getChildren();
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
        int num = 0;
        for (int i = 0; i < funNodeLists.size(); i++) {
            ArrayList<IASTNode> nodeArrayList = statementNodeLists.get(i);
            for (int j = 0; j < nodeArrayList.size(); j++) {
                String name = fileName.substring(0, fileName.indexOf(".")) + "NormalOne" + num;
                printTree(translationUnit1, 1, nodeArrayList.get(j), name);
                num++;
            }
        }

    }

    private static void addNode(IASTNode node2, ArrayList<ArrayList<IASTNode>> statementNodeLists) {
        IASTNode[] nodes = node2.getChildren();
        ArrayList<IASTNode> list = new ArrayList<>();
        for (IASTNode child: nodes) {
            getStatementNode(child, list);
        }
        statementNodeLists.add(list);
    }

    public static void getStatementNode(IASTNode iNode, ArrayList<IASTNode> list){
        IASTNode[] children = iNode.getChildren();
        if(iNode.getClass().getSimpleName().indexOf("Statement") >= 0 && !iNode.getClass().getSimpleName().equals("CASTCompoundStatement")){
            list.add(iNode);
        }

        for (IASTNode child: children) {
            getStatementNode(child, list);
        }
    }

    public static void log(String info, String str) throws IOException {
        OutputStream out = getOutputStream(str);
        out.write(info.getBytes("utf-8"));
        out.write("\r\n".getBytes());
    }

    private static boolean fileLog = true;

    public static OutputStream getOutputStream(String str) throws IOException {
        if (fileLog) {
            File file = new File("D:\\论文资料\\变异\\normalOne\\" + str + ".txt");
            if (!file.exists())
                file.createNewFile();
            return new FileOutputStream(file, true);
        } else {
            return System.out;
        }
    }

    //打印子树
    private static void printTree(IASTNode node, int index, IASTNode node1, String fileName) throws IOException {
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

        if(node.equals(node1)){
            return;
        }else {
            if (node.getClass().getSimpleName().equals("CASTCompoundStatement")) {
                log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                        node.getClass().getSimpleName() + "  " +
                        (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                                node.getRawSignature().subSequence(0, 1)),fileName);
            } else {
                log(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                        node.getClass().getSimpleName() + "  " +
                        (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                                node.getRawSignature().subSequence(0, 5)), fileName);
        }

        }

        for (IASTNode iastNode : children)
            printTree(iastNode, index + 1, node1, fileName);
    }
    //获取编译单元
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

import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.*;

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

public class GetSubtree {

    static ArrayList<ArrayList<String>> lists = new ArrayList<>();

    public static ArrayList<ArrayList<String>> getTree(IASTTranslationUnit translationUnit) throws CoreException {
        printTree(translationUnit, 1);
        for (int i = 0; i < lists.size(); i++){
            if(lists.get(i).size() > 8)
                lists.remove(i);
        }
        return lists;
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

        IASTPreprocessorIncludeStatement[] includes = translationUnit.getIncludeDirectives();
        for (IASTPreprocessorIncludeStatement include : includes) {
            System.out.println("include - " + include.getName());
        }
        return translationUnit; 
    }

    private static void printTree(IASTNode node, int index) {
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

//        if (node.getClass().getSimpleName().equals("CASTCompoundStatement")) {
//            System.out.println(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
//                    node.getClass().getSimpleName() + "  " +
//                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
//                            node.getRawSignature().subSequence(0, 1)));
//        }
        if (node.getClass().getSimpleName().equals("CASTDeclarationStatement")) {
            ArrayList<String>  list=new ArrayList<>();
            printSubtree(node, index, list);
            lists.add(list);
            return;
        }
        //else {
//
//            System.out.println(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
//                    node.getClass().getSimpleName() + "  " +
//                    (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
//                            node.getRawSignature().subSequence(0, 5)));
//        }

        for (IASTNode iastNode : children)
            printTree(iastNode, index + 1);
    }

    private static void printSubtree(IASTNode node, int index, ArrayList<String> list){
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

        list.add(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[]{" "}) +
                node.getClass().getSimpleName() + "  " +
                (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") :
                        node.getRawSignature().subSequence(0, 5)));

        for (IASTNode iastNode : children)
            printSubtree(iastNode, index + 1, list);
    }

    public static ArrayList<String> getMainName(IASTTranslationUnit translationUnit){
        ArrayList<String> list = new ArrayList<>();
        ASTVisitor visitor = new ASTVisitor() {
            public int visit(IASTName name) {
                if ((name.getParent() instanceof CASTFunctionDeclarator)) {
                    list.add(name.getRawSignature());
//                    System.out.println("IASTName: " + name.getClass().getSimpleName() + "(" + name.getRawSignature() + ") - > parent: " + name.getParent().getClass().getSimpleName());
//                    System.out.println("-- isVisible: " + ParserExample.isVisible(name));
                }

                return 3;
            }

//            public int visit(IASTDeclaration declaration) {
//                System.out.println("declaration: " + declaration + " ->  " + declaration.getRawSignature());
//
//                if ((declaration instanceof IASTSimpleDeclaration)) {
//                    IASTSimpleDeclaration ast = (IASTSimpleDeclaration) declaration;
//                    try {
//                        System.out.println("--- type: " + ast.getSyntax() + " (childs: " + ast.getChildren().length + ")");
//                        IASTNode typedef = ast.getChildren().length == 1 ? ast.getChildren()[0] : ast.getChildren()[1];
//                        System.out.println("------- typedef: " + typedef);
//                        IASTNode[] children = typedef.getChildren();
//                        if ((children != null) && (children.length > 0))
//                            System.out.println("------- typedef-name: " + children[0].getRawSignature());
//                    } catch (ExpansionOverlapsBoundaryException e) {
//                        e.printStackTrace();
//                    }
//
//                    IASTDeclarator[] declarators = ast.getDeclarators();
//                    for (IASTDeclarator iastDeclarator : declarators) {
//                        System.out.println("iastDeclarator > " + iastDeclarator.getName());
//                    }
//
//                    IASTAttribute[] attributes = ast.getAttributes();
//                    for (IASTAttribute iastAttribute : attributes) {
//                        System.out.println("iastAttribute > " + iastAttribute);
//                    }
//
//                }
//
//                if ((declaration instanceof IASTFunctionDefinition)) {
//                    IASTFunctionDefinition ast = (IASTFunctionDefinition) declaration;
//                    IScope scope = ast.getScope();
//                    try {
//                        System.out.println("### function() - Parent = " + scope.getParent().getScopeName());
//                        System.out.println("### function() - Syntax = " + ast.getSyntax());
//                    } catch (DOMException e) {
//                        e.printStackTrace();
//                    } catch (ExpansionOverlapsBoundaryException e) {
//                        e.printStackTrace();
//                    }
//                    ICPPASTFunctionDeclarator typedef = (ICPPASTFunctionDeclarator) ast.getDeclarator();
//                    System.out.println("------- typedef: " + typedef.getName());
//                }
//
//                return 3;
//            }
//
//            public int visit(IASTTypeId typeId) {
//                System.out.println("typeId: " + typeId.getRawSignature());
//                return 3;
//            }
//
//            public int visit(IASTStatement statement) {
//                System.out.println("statement: " + statement.getRawSignature());
//                return 3;
//            }
//
//            public int visit(IASTAttribute attribute) {
//                return 3;
//            }
        };
        visitor.shouldVisitNames = true;
        visitor.shouldVisitDeclarations = false;

        visitor.shouldVisitDeclarators = true;
        visitor.shouldVisitAttributes = true;
        visitor.shouldVisitStatements = false;
        visitor.shouldVisitTypeIds = true;

        translationUnit.accept(visitor);
        return list;
    }
}

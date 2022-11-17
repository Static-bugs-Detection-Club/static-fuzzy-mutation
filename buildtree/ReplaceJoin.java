package buildtree;

import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReplaceJoin implements Mutation{
    public ArrayList<Node> main(ArrayList<Node> list) throws IOException, CoreException {
        ArrayList<Node> reNode = new ArrayList<>();
        for (Node child: list) {
            deleteJoin(child);
            reNode.add(child);
        }
        return reNode;
    }

    public static void deleteJoin(Node node){
        List<Node> sonList = node.getSonList();

        if(sonList == null || sonList.size() == 0){
            return;
        }else {
            for (int i = 0; i < sonList.size(); i++) {
                if(sonList.get(i).getNodeValue().indexOf("pthread_join") >= 0 && sonList.get(i).getNodeName().equals("CASTExpressionStatement")){
                    sonList.remove(i);
                    i--;
                    continue;
                }
                deleteJoin(sonList.get(i));

            }
        }

    }
}

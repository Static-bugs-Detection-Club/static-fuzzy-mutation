package buildtree;

import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.util.ArrayList;

public interface Mutation {
    ArrayList<Node> main(ArrayList<Node> list) throws IOException, CoreException;
}

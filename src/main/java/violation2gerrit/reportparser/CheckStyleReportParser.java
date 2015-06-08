package violation2gerrit.reportparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import violation2gerrit.Comment;

public class CheckStyleReportParser implements ReportParser {

	public CheckStyleReportParser() {
	}

	@Override
	public List<Comment> parse(InputStream stream) throws ReportParseException {
		try {
			List<Comment> comments = new ArrayList<>();
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
			Element root = dom.getDocumentElement();
			NodeList fileNodeList = root.getElementsByTagName("file");
			for(int i = 0; i < fileNodeList.getLength(); i++) {
				Node fileNode = fileNodeList.item(i);
				String file = fileNode.getAttributes().getNamedItem("name").getTextContent();
				NodeList errorNodeList = fileNode.getChildNodes();
				for(int j = 0; j < errorNodeList.getLength(); j++) {
					Node errorNode = errorNodeList.item(j);
					if(!"error".equals(errorNode.getNodeName()))
						continue;
					String msg = errorNode.getAttributes().getNamedItem("severity").getTextContent() 
							+ " : " 
							+ errorNode.getAttributes().getNamedItem("message").getTextContent();
					Comment com = new Comment(
							file,
							errorNode.getAttributes().getNamedItem("line").getTextContent(),
							msg
						);
					comments.add(com);
				}
			}
			return comments;
		} catch(IOException | SAXException | ParserConfigurationException e) {
			throw new ReportParseException(e);
		}
	}
}

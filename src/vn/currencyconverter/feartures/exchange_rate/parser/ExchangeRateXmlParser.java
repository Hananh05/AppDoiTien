package vn.currencyconverter.feartures.exchange_rate.parser;

import vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateXmlParser {

    public static List<ExchangeRate> parse(String xmlData) {
        List<ExchangeRate> rates = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlData)));

            NodeList nList = doc.getElementsByTagName("Exrate");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String code = element.getAttribute("CurrencyCode");
                    String name = element.getAttribute("CurrencyName");

                    String buyStr = element.getAttribute("Buy").replace(",", "").trim();
                    String sellStr = element.getAttribute("Sell").replace(",", "").trim();

                    BigDecimal buyRate = buyStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(buyStr);
                    BigDecimal sellRate = sellStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(sellStr);

                    rates.add(new ExchangeRate(code, name, buyRate, sellRate));
                }
            }
        } catch (Exception e) {
            System.err.println("[Backend Error] Bóc tách XML thất bại: " + e.getMessage());
        }
        return rates;
    }
}
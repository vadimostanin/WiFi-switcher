package finder.remotedevice.vostanin.com.remotedevicefinder;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vostanin on 2/12/16.
 */
public class HtmlHelper {
    TagNode     mRootNode;
    Document     mRootDocument;

//    HtmlCleaner mCleaner = null;


    //Конструктор
    public HtmlHelper( URL url ) throws IOException
    {
        //Создаём объект HtmlCleaner
//        mCleaner = new HtmlCleaner();
        //Загружаем html код сайта
//        mRootNode = mCleaner.clean( htmlPage );
        mRootDocument = Jsoup.connect( url.toString() ).get();
    }

    public HtmlHelper( File file )
    {
        try
        {
            String htmlPage = Utils.readFile( file.getPath() );
            mRootDocument = Jsoup.parse( htmlPage );
        }
        catch( Exception e )
        {

        }
    }

    //Конструктор
    public HtmlHelper( String htmlPage) throws IOException
    {
        mRootDocument = Jsoup.parse(htmlPage);
    }

    public Elements getLinksByClass(String CSSClassname)
    {
        return getElementsByClass( CSSClassname );
    }

    public Elements getDivsByClass(String CSSClassname)
    {
        return getElementsByClass( CSSClassname );
    }

//    List<TagNode> getLinksByClass(String CSSClassname)
//    {
//        return getElementsByClass( "a", CSSClassname );
//    }
//
//    List<TagNode> getDivsByClass(String CSSClassname)
//    {
//        return getElementsByClass( "div", CSSClassname );
//    }
//
//    String getInnerHtml( TagNode root )
//    {
//        return mCleaner.getInnerHtml(root);
//    }

    public Elements getElementsByClass( Element rootNode, String CSSClassname)
    {
        //Выбираем все ссылки
        Elements linkElements = rootNode.getElementsByClass( CSSClassname );
        return linkElements;
    }

    public List<Element> getElementH1ByClass( String CSSClassname )
    {
        return getElementsByClass( "H1", CSSClassname );
    }

    public List<Element> getElementH3ByClass( String CSSClassname )
    {
        return getElementsByClass( "H3", CSSClassname );
    }

    public List<Element> getElementsImage()
    {
        return mRootDocument.getElementsByTag( "img" );
    }

    private Elements getElementsByClass( String CSSClassname )
    {
        //Выбираем все ссылки
        Elements linkElements = mRootDocument.getElementsByClass(CSSClassname);
        return linkElements;
    }

    private List<Element> getElementsByClass( String tag, String CSSClassname )
    {
        List<Element> result = new ArrayList<>();
        //Выбираем все ссылки
        Elements taggedElements = mRootDocument.getElementsByTag( tag );
        for ( Element el: taggedElements )
        {
            String classValue = el.attr( "class" );
            if( true == classValue.contentEquals( CSSClassname ) )
            {
                result.add( el );
            }
        }
        return result;
    }

//    public static List<TagNode> getElementsByClass( TagNode rootNode, String CSSTagname, String CSSClassname)
//    {
//        List<TagNode> linkList = new ArrayList<TagNode>();
//
//        //Выбираем все ссылки
//        TagNode linkElements[] = rootNode.getElementsByName(CSSTagname, true);
//        for (int i = 0; linkElements != null && i < linkElements.length; i++)
//        {
//            //получаем атрибут по имени
//            String classType = linkElements[i].getAttributeByName("class");
//            //если атрибут есть и он эквивалентен искомому, то добавляем в список
//            if (classType != null && classType.equals(CSSClassname))
//            {
//                linkList.add(linkElements[i]);
//            }
//        }
//
//        return linkList;
//    }

//    private List<TagNode> getElementsByClass(String CSSTagname, String CSSClassname)
//    {
//        List<TagNode> linkList = new ArrayList<TagNode>();
//
//        //Выбираем все ссылки
//        TagNode linkElements[] = mRootNode.getElementsByName(CSSTagname, true);
//        for (int i = 0; linkElements != null && i < linkElements.length; i++)
//        {
//            //получаем атрибут по имени
//            String classType = linkElements[i].getAttributeByName("class");
//            //если атрибут есть и он эквивалентен искомому, то добавляем в список
//            if (classType != null && classType.equals(CSSClassname))
//            {
//                linkList.add(linkElements[i]);
//            }
//        }
//
//        return linkList;
//    }
}

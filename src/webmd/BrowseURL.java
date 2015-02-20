/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webmd;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;



/**
 *
 * @author Kalyan
 */

/*
This class is to open urls in browser so we can check the hon code easily
*/


class BrowseURL {
    

    public void openURLlist(List<String> urls) throws IOException, URISyntaxException{
        Desktop d = Desktop.getDesktop();
        for(String eachURL : urls){
            d.browse(new URI(eachURL));
        }
    }
    
    public void openURL(String eachURL) throws IOException, URISyntaxException{
        Desktop d = Desktop.getDesktop();
            d.browse(new URI(eachURL));
    }
}

package alma.irm;

import java.util.Date;

import org.sikuli.script.App;
import org.sikuli.script.FindFailed;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ot {
	
	private Screen s;
	private App ffox;
	private String myImagePath = System.getProperty("user.dir") + "/test/src/alma/irm/images/";
	
	//to differentiate hotkeys
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	@Parameters({ "platform", "url"})
	@BeforeTest (alwaysRun=true, groups = {"createNewProposal", "getProposalFromArchive"})
	public void setup(String platform, String url){
		//Here's where we get images from
		ImagePath.setBundlePath(myImagePath);
		//s is the screen we'll search in. Defaults to 0, in case there's multiscreen
		s = new Screen();
		try{
			//we'll get the webstart using ffox, as it's available on mac, win and lux
			ffox = App.open("firefox");
			//need a way to wait for the window to pop up
			Date date  = new Date(); 
			long startTime = date.getTime(); 
			long delta = 0;
			while (!ffox.hasWindow() && delta < 1){
				ffox.waitForWindow();
				Date date2  = new Date();
				long stopTime = date2.getTime();
				//delta is stored on minutes.
				delta = (stopTime - startTime)/ (long)(60 * 1000);
				System.out.println(delta);
			}
			//send a hotkey to go to the address bar
			if (OS.indexOf("mac") >= 0){
				s.type("l", KeyModifier.CMD);
			}else if (OS.indexOf("win") >= 0){
				s.type("l", KeyModifier.CTRL);
			}else if (OS.indexOf("lin") >= 0){
				s.type("l", KeyModifier.CTRL);
			}else {
				ffox.close();
				Assert.assertEquals(true, false, "OS reported is " + OS + " and is not recognized");
			}
			//clean the address bar
			s.type(Key.BACKSPACE);
			s.write( url + "#ENTER." );
		}
		catch(Exception e){
			e.printStackTrace();
			ffox.close();
			Assert.assertEquals(true, false);
		}
		
	}
	
	@Test (groups = {"createNewProposal", "getProposalFromArchive"})
	public void getWebstartOT() throws Exception{
		try{
			//click on the download link
			s.click("otWebStart.png");
			//wait for the confirmation dialog. We assume
			//the default action is to open it
			Pattern cancelOk = new Pattern ("ffox_cancel_ok_mac_themed.png");
			s.click(cancelOk.similar((float)0.2));
			s.wait("ffox_cancel_ok_mac_themed.png");
			//ok to download it
			s.click("ffox_ok_mac_themed.png");
			//after download, let't open it
			//the long timeout is due to the downloading/verification of the application
			s.wait("ffox_open_mac_themed.png",120);
			s.click();
			//java security warning (only for non-signed servers) on the first time we ran the OT
			if ((s.exists("java_confirmation_mac_themed.png", 15) == null) &&
				(s.exists("OT_startup_options.png",5) == null)){
			}else{
				s.wait("java_confirmation_mac_themed.png",120);
				s.click("checkbox.png");
				s.click("run_button_mac_themed.png");
			}
			//wait for OT startup options to be displayed
			s.wait("OT_startup_options.png", 120);
			//validate we're good so far
			//validate we see the main project info section
			Assert.assertEquals(true, s.exists("OT_startup_options.png") != null);
			//after this we won't need firefox
			ffox.close();
		}
		catch(FindFailed e){
			e.printStackTrace();
			Assert.assertEquals(true, false);
			ffox.close();
		}
		
	}
	
	@Test(dependsOnMethods = {"getWebstartOT"} , groups = {"createNewProposal"})
	public void createNewProposal(){
		try{
			s.click("createnewProposal.png");
			s.click("OT_ok.png");
			//to handle eventual OT messages regarding newer versions
			if (s.exists("OT_information_icon.png") != null){
				s.click("OT_information_icon.png");
				s.write("#ESC.");
				//validate we see the main project info section
				Assert.assertEquals(true, s.exists("OT_assert_create_proposal.png") != null);
			}
		}
		catch(FindFailed e){
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}

	
	@Parameters({ "username", "password"})
	@Test(dependsOnMethods = {"createNewProposal"}, groups = {"getProposalFromArchive"})
	public void retrieveProposalFromArchive(String username, String password){
		try{
			//to handle eventual OT messages
			if (s.exists("OT_information_icon.png") != null){
				s.click("OT_information_icon.png");
				s.write("#ESC.");
			}
			//search all my projects
			s.click("OT_file_menu.png");
			s.click("OT_open_project.png");
			s.click("OT_from_ALMA_archive.png");
			s.click("OT_all_my_projects.png");
			s.click("OT_search_button.png");
			//used pattern as we need to click at the lef side of the label
			Pattern ALMAID = new Pattern("OT_ALMAID_input.png");
			s.wait(ALMAID, 10);
			s.click(ALMAID.exact().targetOffset(100, 0));
			s.doubleClick();
			s.write(username);
			//same for the password field
			Pattern PWD = new Pattern("OT_password_input.png");
			s.click(PWD.exact().targetOffset(100, 0));
			s.doubleClick();
			s.write(password + "#ENTER.");
			//let's validate we found our proposals
			Assert.assertEquals(true, s.exists("OT_assert_retrieve_proposal.png") != null);
			
		}
		catch(FindFailed e){
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}
	
	@AfterTest(dependsOnGroups={"createNewProposal", "getProposalFromArchive"})
	public void tearDown(){
		try{
			for (int i=0 ; i<3; i++){
				s.write("#ESC.");
			}
			s.click("OT_file_menu.png");
			s.click("OT_file_menu_quit.png");
			
		}
		catch(FindFailed e){
			e.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}
	

}

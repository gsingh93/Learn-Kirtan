<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"
         "../dtd/helpset_2_0.dtd">

<helpset version="1.0">

  <!-- title -->
  <title>Learn Kirtan Help</title>

  <!-- maps -->
  <maps>
     <homeID>top</homeID>
     <mapref location="map.jhm"/>
  </maps>

  <!-- views -->
  <view>
    <name>TOC</name>
    <label>Table Of Contents</label>
    <type>javax.help.TOCView</type>
    <data>SampleTOC.xml</data>
  </view>

  <presentation default="true" displayviewimages="false">
     <name>main window</name>
     <size width="700" height="600" />
     <location x="300" y="70" />
     <title>Learn Kirtan Help</title>
     <image>toplevelfolder</image>
     <toolbar>
		<helpaction>javax.help.BackAction</helpaction>
		<helpaction>javax.help.ForwardAction</helpaction>
		<helpaction>javax.help.SeparatorAction</helpaction>
		<helpaction>javax.help.HomeAction</helpaction>
		<helpaction>javax.help.SeparatorAction</helpaction>
		<helpaction>javax.help.PrintAction</helpaction>
		<helpaction>javax.help.PrintSetupAction</helpaction>
     </toolbar>
  </presentation>
  <presentation>
     <name>main</name>
     <size width="400" height="400" />
     <location x="200" y="200" />
     <title>Learn Kirtan Help</title>
  </presentation>
</helpset>
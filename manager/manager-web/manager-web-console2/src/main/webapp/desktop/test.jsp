<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="../lib/ext-4.2.1.883/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css"
	href="../lib/ext-4.2.1.883/examples/desktop/css/desktop.css" />
<script type="text/javascript"
	src="../lib/ext-4.2.1.883/ext-all-debug-w-comments.js"></script>
<script type="text/javascript" src="../zero/ex/extend.js"></script>
<script type="text/javascript" src="../zero/util/zero-collection.js"></script>
</head>
<body>
</body>
<script type="text/javascript">
	<%
		String viewName = request.getParameter("view");
	%>
	
	var viewName = '<%=viewName %>'
	Ext.Loader.setConfig({
		enabled : true,
	});
	//	    disableCaching: (/^true$/i).test(config.extjs_ajax_disable_cache)

	Ext.Loader.setPath({
		'Ext.ux.desktop' : '../lib/ext-4.2.1.883/examples/desktop/js',
		'MyDesktop' : '../lib/ext-4.2.1.883/examples/desktop',
		'Zero' : '../zero',
	});

	Ext.require('App');

	Ext.EventManager.on(document, 'keydown', function(e, t) {
		if (e.getKey() == e.BACKSPACE && (!/^input$/i.test(t.tagName) || t.disabled || t.readOnly)
				&& (!/^textarea/i.test(t.tagName) || t.disabled || t.readOnly)) {
			e.stopEvent();
		}
	});
	var myDesktopApp;
	Ext.onReady(function() {
		Zero.app = {};
		Zero.app.controllers = new Map();

		var configView = Ext.create(viewName, {
			autoScroll : true,
			autoWidth : true,
			height : 350,
			width : 600,
			renderTo : Ext.getBody()
		});

		/* 
			Ext.create("Ext.panel.Panel",{
				layout:'fit',
				items: [{
					xtype:'viewport',
					layout: { type: "vbox", align: 'stretch' },
					items: [
			                {
			                    html: "Hello!"
			                },
			                {
			                    xtype: "grid",
			                    title: 'Simpsons Contacts',
			                    store: propStore,
			                    flex: 1,
			                    columns: [
			                        { text: 'Name', dataIndex: 'prop_Key' },
			                        { text: 'Email', dataIndex: 'prop_Val', flex: 1 },
			                        { text: 'Phone', dataIndex: 'prop_Desc' }
			                    ]
			                }
			            ]
				}],
				renderTo:Ext.getBody()
			}) */

		// 동적 Controller 로딩을 위해 네임스페이스에 Desktop Setting.
		console.log("Desktop Constructor ", Zero.app);
	});
</script>
</html>
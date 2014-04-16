/**
 * 
 */
Ext.define('Zero.view.desktop.Install', {
	extend : 'Ext.ux.desktop.Module',
	requires:['Zero.view.base.ServerGroup',
	          'Zero.view.desktop.LeftMenu',
	          'Zero.view.desktop.ContentTab'],
	id : 'installer',
	title : 'Installer',
	init : function() {
		this.launcher = {
			text : 'Installer',
			iconCls : 'install'
		};
	},
	createWindow:function () {
		var me = this;
		var desktop = this.app.getDesktop();
		console.log(desktop);
		var win = desktop.getWindow(me.id);
		console.log("Is Window Exists?",win);
		if(!win) {
			win = desktop.createWindow({
				id:me.id,
				title:me.title,
				width:1024,
				height:600,
				hideMode:'offset',
				layout:'border',
				items:[{
					xtype:'desktopLeftMenu',
					region:'west',
					width : 150,
				},
				{
					xtype:'desktopContentTab',
					region:'center'
				}
				]
			});
		}
		
		return win;
	}

});
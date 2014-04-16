/**
 * 
 */
Ext.define('Zero.view.desktop.ContentTab',{
	extend: 'Ext.tab.Panel',
	alias: 'widget.desktopContentTab',
	initComponent:function () {
		console.log("desktopContentTab");
		var me = this;
		this.items = [
		];
		me.callParent(arguments);
	}
});
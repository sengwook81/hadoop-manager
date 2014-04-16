/**
 * 
 */
Ext.define('Zero.view.desktop.LeftMenuItem',{
	extend: 'Ext.tree.Panel',
	alias: 'widget.desktopLeftMenuItem',
	border: 0,
    autoScroll: true,
    rootVisible: false,
 
    initComponent: function() {
        var me = this;
 
        me.callParent(arguments);
    }
});
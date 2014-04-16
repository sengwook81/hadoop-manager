/**
 * 
 */
Ext.define('Zero.view.desktop.LeftMenu',{
	extend: 'Zero.view.AbstractZeroView',
	requires:['Zero.view.desktop.LeftMenuItem'],
	controllers:['Zero.controller.desktop.DesktopCTR'],
	alias: 'widget.desktopLeftMenu',
	initComponent:function () {
		console.log("desktopLeftMenu");
		var me = this;
		Ext.apply(me,{
			title:'Menu',
			 layout: {
			        // layout-specific configs go here
			        type: 'accordion',
			        titleCollapse: false,
			        animate: true,
			        activeOnTop: true
		    },
			items : [ {
				xtype:'desktopLeftMenuItem',
				title : '기본정보 관리',
				group : 'base'
				
			}, {
				title : 'APP 설치 관리',
				xtype:'desktopLeftMenuItem',
				group : 'app'
			} ]
		});
		me.callParent(arguments);
	}
});
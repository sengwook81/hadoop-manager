/**
 * 
 */
Ext.define('Zero.controller.install.NodeCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'install.Nodes'
	],
	init:function () {
		me = this;
		me.control({
			'#installnode gridpanel' :{
				itemclick:function (el , record , item , index) {
					console.log("Gridpanel Item Click", el , record , item , index);
				}
			},
			'#installnode' : {
				afterrender:function () {
				}
			}
		}
		);
		me.callParent();
	}
});
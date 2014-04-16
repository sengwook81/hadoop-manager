Ext.application({
	name : 'Zero',
	appFolder:"",
	//controllers : [ 'Common.controller.BaseController' ],//,'Common.controller.MessageController'],
	requires:['Ext.Viewport'],
	launch:function(){
		viewport = Ext.create('Ext.Viewport', {
		});
		Zero.app = this;  //Added for global reference to application instance. (This should be possible to do smarter)
		Zero.app.viewport = viewport;
		console.log("Init Finish");
	}
});
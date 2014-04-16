/**
 * Grid 내 CheckBox Delete용도의 컴포넌트.
 */
Ext.define('Zero.ex.grid.CheckColumn', {
	extend : 'Ext.grid.column.CheckColumn',
	alias:'widget.excheckcolumn',
	dataIndex : 'del_Yn',
	stopSelection : false,
	initComponent:function () {
		var me = this;
		me.callParent();
	},
	listeners : {
		// 해당 체크박스 선택시 NewRow인 경우 해당 Row를 바로 삭제처리한다.
		checkchange : function(el, rowIndex, checked, opts) {
			console.log("CheckBox Change ", arguments, el.up('grid').getStore());
			console.log("Changed Row : ", el.up('grid').getStore().getAt(rowIndex));
			record = el.up('grid').getStore().getAt(rowIndex);
			if (record.phantom) {
				el.up('grid').getStore().remove(record);
			}
		}
	}
});
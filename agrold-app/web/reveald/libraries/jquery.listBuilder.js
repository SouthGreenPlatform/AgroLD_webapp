/**
* List Builder
*
* @fileoverview jQuery List Builder uses the jQuery UI autocomplete to convert a basic input into an email 
* client like "To" field with autocompletion.
* @link https://github.com/th3uiguy/jquery-listbuilder
* @author Spencer Neese
* @version 1.0
* @requires jQuery UI 1.7+ and jQuery 1.3.2+
* @license jQuery List Builder Plugin v1.0
*
* Copyright 2012, Spencer Neese
* Dual licensed under the MIT or GPL Version 2 licenses.
* <https://raw.github.com/th3uiguy/jquery-listbuilder/master/GPL-LICENSE.txt> <https://raw.github.com/th3uiguy/jquery-listbuilder/master/MIT-LICENSE.txt>
*/
;(function( $ ){
	
$.fn.listBuilder = function(options){
	
	if (typeof options === 'string') {
		var args = Array.prototype.slice.call(arguments, 1);
		return $(this).triggerHandler('listbuilder'+options, args);
	}

	var defaults = {
		maxItems: 0,
		onAddButtonClick: null,
		onlyAddFromSource: true,
		beforeAddItem: null,
		items: [],
		onAddItem: null,
		beforeRemoveItem: null,
		onMaxReached: null,
		showRemoveIcon: true,
		autocompleteOptions: {
			autoFocus: true,
			focus: function (ev) { ev.preventDefault(); },
			source: []
		}
	};
	
	var opts = $.extend(true, {}, defaults, options);
	var shiftDown = false;
	var ctrlDown = false;
	var inInput = false;
	var itemCount = 0;
	var $inputField = $(this);
	var $container = $('<div class="listBuilder"><ul><li></li></ul></div>').insertBefore($inputField);
	
	$container.find('li').append($inputField);
	if(typeof opts.onAddButtonClick == "function"){
		var $addBtn = $('<a href="javascript:void(0)" class="add ui-state-default ui-corner-all"><span class="ui-icon ui-icon-plusthick"></span></a>');
		$addBtn
			.click(opts.onAddButtonClick)
			.hover(
				function(){ $(this).addClass('ui-state-hover'); },
				function(){ $(this).removeClass('ui-state-hover'); }
			);
		$container.prepend($addBtn);
	}
	
	var self = this;
	//TODO: use jQuery ui keyCodes
	var Keys = {
		ENTER: 13,
		BACKSPACE: 8,
		DELETE: 46,
		SHIFT: 16,
		CTRL: 17,
		CMD: 224
	};
	
	$inputField.autocomplete(opts.autocompleteOptions);
	
	$(self)
	.bind('listbuilderadd', function(ev, item){
		_addListItem($container, item, false);
		return $(self);
	})
	.bind('listbuildercount', function(ev, val){
		return itemCount;
	});
	
	$(window).keydown(function(ev){ 
		shiftDown = (ev.keyCode == Keys.SHIFT);
		ctrlDown = (ev.keyCode == Keys.CTRL || ev.keyCode == Keys.CMD);
	});
	$(window).keyup(function(ev){ 
		switch(ev.keyCode){
			case Keys.SHIFT:
				shiftDown = false;
				break;
			case Keys.CTRL:
			case Keys.CMD:
				ctrlDown = false;
				break;
		}
	});
	$(window).keypress(function(ev){ 
		// console.log([ev.keyCode, $.keyCodes])
		if(ev.keyCode == Keys.DELETE || ev.keyCode == Keys.BACKSPACE){ //delete key
			var activeItems = $container.find('ul li:has(a.active)');
			if(activeItems.size() > 0){
				activeItems.each(function(){
					_removeListItem($(this));
				});
				return false;
			}
			else if(inInput && ev.keyCode == Keys.BACKSPACE && $inputField.val().length == 0){
				var activeCount = $container.find('li a').not('.locked').last().addClass('active').size();
				if(activeCount > 0) $inputField.blur();
			}
		}
		else if(ev.keyCode == Keys.ENTER && inInput && opts.onlyAddFromSource == false){
			var txt = $inputField.val();
			$inputField.val('');
			_addListItem($container, {label:txt, value:txt});
		}
		else{
			// console.log(ev.keyCode);
		}
	});
	if(opts.onlyAddFromSource != false){
		$inputField.bind('autocompleteselect', function(ev, ui){
			if(typeof opts.beforeAddItem == "function"){
				opts.beforeAddItem(ui.item, function(result){
					if(result === true){
						$inputField.val('');
						_addListItem($container, ui.item);
					}
					else{
						$inputField.val('').focus();
					}
				});
			}
			else{
				$inputField.val('');
				_addListItem($container, ui.item);
			}
			return false;
		});
	}
	else{
		$inputField.bind('autocompleteselect', function(ev, ui){
			return false;
		});
	}
	
	$container.click(function(){
		$container.find('a:not(.add)').removeClass('active');
		if(opts.maxItems == 0 || (opts.maxItems > 0 && itemCount < opts.maxItems)){
			$inputField.focus();
		}
	});
	$container.find('a:not(.add)').bind('click.listbuilder', _itemClick);
	$inputField
		.focus(function(){
			$container.addClass('lb-active');
			inInput = true;
		})
		.blur(function(){
			$container.removeClass('lb-active');
			inInput = false;
			if(this.value.length){
				_addListItem($container, {label:this.value, value:this.value}, false);
				$container.find('a:last').addClass('invalid').prepend('<span class="ui-icon ui-icon-alert"></span>');
				this.value = '';
			}
		});

	if($.isArray(opts.items) && opts.items.length > 0){
		var items = opts.items;
		for(var i in items){
			_addListItem($container, items[i], false);
		}
	}

	return $(self);
	
	function _addListItem($list, item, doExtra){
		var label = item.label || item, value = item.value || label, locked = item.locked;
		if(opts.maxItems > 0 && itemCount+1 > opts.maxItems){
			if(typeof opts.onMaxReached == "function"){
				$inputField.autocomplete("option", "disabled", true ).css('display', 'none').blur();
				opts.onMaxReached(item, $list);
			}
			return;
		}
		var $href = $('<a href="javascript:void(0)">' + label + '</a>');
		if(opts.showRemoveIcon == true && locked != true){
			var $removeIcon = $('<span class="ui-icon ui-icon-closethick"></span>')
				.click(function(){
					_removeListItem($(this).closest('li'));
				})
				.hover(
					function(){ $(this).closest('a').addClass('lb-hover'); },
					function(){ $(this).closest('a').removeClass('lb-hover'); }
				);
			$href.append($removeIcon);
		}
		var $val = $('<input type="hidden" /> ').attr('name', $(self).attr('name')).val(value).data('itemData', item);
		var $item = $('<li></li>').append($href).append($val);
		$item.find('a').click(_itemClick);
		if(locked === true) $item.find('a').addClass('locked');
		$item.insertBefore($list.find('li:last'));
		itemCount++;
		if(opts.maxItems !== 0 && opts.maxItems > 0 && itemCount >= opts.maxItems){ //if max is reached, disable adding more
			$inputField.autocomplete("option", "disabled", true).prop("disabled", true).hide();
		}
		if(doExtra != false && typeof opts.onAddItem == "function") opts.onAddItem(item);
		
	}
	function _removeListItem(item){
		if(typeof opts.beforeRemoveItem == "function"){
			var toRemove = $(item).find('input').data('itemData');
			// item.hide();
			opts.beforeRemoveItem(toRemove, function(result){
				if(result == true) _completeRemove(item);
				// else item.show();
			});
		}
		else{
			_completeRemove(item);
		}
	}
	function _completeRemove(item){
		itemCount --;
		item.remove();
		if(opts.maxItems === 0 || (opts.maxItems > 0 && itemCount < opts.maxItems)){ //if max is not reached, enable adding more
			$inputField.autocomplete( "option", "disabled", false ).prop("disabled", false).show();
		}
		$inputField.focus();
	}
	function _itemClick(ev){
		var item = ev.target;
	
		if(!shiftDown && !ctrlDown) $container.find('a').removeClass('active');
		if($(item).hasClass('active')) $(item).removeClass('active');
		else $(item).not('.locked').addClass('active');
		return false;
	}
}
})(jQuery);
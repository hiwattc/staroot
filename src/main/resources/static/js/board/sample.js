/**
 * Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

/* exported initSample */

if ( CKEDITOR.env.ie && CKEDITOR.env.version < 9 )
	CKEDITOR.tools.enableHtml5Elements( document );

// The trick to keep the editor in the sample quite small
// unless user specified own height.


if(window.innerWidth < 500){
	CKEDITOR.config.height = 200;
}else{
	CKEDITOR.config.height = 500;
}

CKEDITOR.config.uiColor = "#FFFFFF";
CKEDITOR.config.language = "ko";
//CKEDITOR.config.removeButtons = "About CKEditor,Flash";

//var btn_grp_document="mode,document,doctools,Underline";
//CKEDITOR.config.removeButtons = "About,Flash"+","+btn_grp_document;


//Toolbar configuration generated automatically by the editor based on config.toolbarGroups.
CKEDITOR.config.toolbar = [
	{ name: 'clipboard', groups: [ 'clipboard', 'undo' ], items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
	{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ], items: [ 'Scayt' ] },
	{ name: 'links', items: [ 'Link', 'Unlink', 'Anchor' ] },
	{ name: 'insert', items: [ 'Image', 'Table', 'HorizontalRule', 'SpecialChar' ] },
	{ name: 'tools', items: [ 'Maximize' ] },
	{ name: 'document', groups: [ 'mode', 'document', 'doctools' ], items: [ 'Source' ] },
	{ name: 'others', items: [ '-' ] },
	'/',
	{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ], items: [ 'Bold', 'Italic', 'Strike', '-', 'RemoveFormat' ] },
	{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ], items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote' ] },
	{ name: 'styles', items: [ 'Styles', 'Format' ] },
	{ name: 'about', items: [ 'About' ] }
];

// Toolbar groups configuration.
CKEDITOR.config.toolbarGroups = [
	{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
	{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ] },
	{ name: 'links' },
	{ name: 'insert' },
	{ name: 'forms' },
	{ name: 'tools' },
	{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
	{ name: 'others' },
	'/',
	{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
	{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
	{ name: 'styles' },
	{ name: 'colors' },
	{ name: 'about' }
];
	  //CKEDITOR.config.extraPlugins = 'autogrow';
//CKEDITOR.config.autoGrow_minHeight = 200;
//CKEDITOR.config.autoGrow_maxHeight = 600;
//CKEDITOR.config.autoGrow_bottomSpace = 50;


CKEDITOR.config.width = 'auto';

var initSample = ( function() {
	var wysiwygareaAvailable = isWysiwygareaAvailable(),
		isBBCodeBuiltIn = !!CKEDITOR.plugins.get( 'bbcode' );

	return function() {
		var editorElement = CKEDITOR.document.getById( 'contents' );

		// :(((
		if ( isBBCodeBuiltIn ) {
			//alert("isBBCodeBuiltIn:true");
			editorElement.setHtml(
				'Hello world1234!\n\n' +
				'I\'m an instance of [url=http://ckeditor.com]CKEditor[/url].'
			);
		}

		// Depending on the wysiwygare plugin availability initialize classic or inline editor.
		if ( wysiwygareaAvailable ) {
			//alert("wysiwygareaAvailable:true");
			//CKEDITOR.inline( 'editor' );
			CKEDITOR.replace( 'contents' ,
			//CKEDITOR.inline( 'contents' ,
					
					 {
						    //filebrowserBrowseUrl: '/ckeditor/file/files',
						    filebrowserBrowseUrl: '/ckeditor/file/editorFileList',
						    filebrowserUploadUrl: '/ckeditor/file/single_upload'
					 }
			);
		} else {
			//alert("wysiwygareaAvailable:false");
			editorElement.setAttribute( 'contenteditable', 'true' );
			CKEDITOR.inline( 'contents' );

			// TODO we can consider displaying some info box that
			// without wysiwygarea the classic editor may not work.
		}
	};

	function isWysiwygareaAvailable() {
		// If in development mode, then the wysiwygarea must be available.
		// Split REV into two strings so builder does not replace it :D.
		if ( CKEDITOR.revision == ( '%RE' + 'V%' ) ) {
			return true;
		}

		return !!CKEDITOR.plugins.get( 'wysiwygarea' );
	}
} )();




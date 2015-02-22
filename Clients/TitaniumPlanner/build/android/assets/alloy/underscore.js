(function(){var e=this,t=e._,r={},i=Array.prototype,n=Object.prototype,o=Function.prototype,a=i.push,s=i.slice,l=i.concat,u=n.toString,c=n.hasOwnProperty,d=i.forEach,h=i.map,p=i.reduce,f=i.reduceRight,_=i.filter,g=i.every,y=i.some,v=i.indexOf,m=i.lastIndexOf,T=Array.isArray,A=Object.keys,I=o.bind,b=function(e){return e instanceof b?e:this instanceof b?void(this._wrapped=e):new b(e)};"undefined"!=typeof exports?("undefined"!=typeof module&&module.exports&&(exports=module.exports=b),exports._=b):e._=b,b.VERSION="1.4.4";var w=b.each=b.forEach=function(e,t,i){if(null!=e)if(d&&e.forEach===d)e.forEach(t,i);else if(e.length===+e.length){for(var n=0,o=e.length;o>n;n++)if(t.call(i,e[n],n,e)===r)return}else for(var a in e)if(b.has(e,a)&&t.call(i,e[a],a,e)===r)return};b.map=b.collect=function(e,t,r){var i=[];return null==e?i:h&&e.map===h?e.map(t,r):(w(e,function(e,n,o){i[i.length]=t.call(r,e,n,o)}),i)};var E="Reduce of empty array with no initial value";b.reduce=b.foldl=b.inject=function(e,t,r,i){var n=arguments.length>2;if(null==e&&(e=[]),p&&e.reduce===p)return i&&(t=b.bind(t,i)),n?e.reduce(t,r):e.reduce(t);if(w(e,function(e,o,a){n?r=t.call(i,r,e,o,a):(r=e,n=!0)}),!n)throw new TypeError(E);return r},b.reduceRight=b.foldr=function(e,t,r,i){var n=arguments.length>2;if(null==e&&(e=[]),f&&e.reduceRight===f)return i&&(t=b.bind(t,i)),n?e.reduceRight(t,r):e.reduceRight(t);var o=e.length;if(o!==+o){var a=b.keys(e);o=a.length}if(w(e,function(s,l,u){l=a?a[--o]:--o,n?r=t.call(i,r,e[l],l,u):(r=e[l],n=!0)}),!n)throw new TypeError(E);return r},b.find=b.detect=function(e,t,r){var i;return x(e,function(e,n,o){return t.call(r,e,n,o)?(i=e,!0):void 0}),i},b.filter=b.select=function(e,t,r){var i=[];return null==e?i:_&&e.filter===_?e.filter(t,r):(w(e,function(e,n,o){t.call(r,e,n,o)&&(i[i.length]=e)}),i)},b.reject=function(e,t,r){return b.filter(e,function(e,i,n){return!t.call(r,e,i,n)},r)},b.every=b.all=function(e,t,i){t||(t=b.identity);var n=!0;return null==e?n:g&&e.every===g?e.every(t,i):(w(e,function(e,o,a){return(n=n&&t.call(i,e,o,a))?void 0:r}),!!n)};var x=b.some=b.any=function(e,t,i){t||(t=b.identity);var n=!1;return null==e?n:y&&e.some===y?e.some(t,i):(w(e,function(e,o,a){return n||(n=t.call(i,e,o,a))?r:void 0}),!!n)};b.contains=b.include=function(e,t){return null==e?!1:v&&e.indexOf===v?-1!=e.indexOf(t):x(e,function(e){return e===t})},b.invoke=function(e,t){var r=s.call(arguments,2),i=b.isFunction(t);return b.map(e,function(e){return(i?t:e[t]).apply(e,r)})},b.pluck=function(e,t){return b.map(e,function(e){return e[t]})},b.where=function(e,t,r){return b.isEmpty(t)?r?null:[]:b[r?"find":"filter"](e,function(e){for(var r in t)if(t[r]!==e[r])return!1;return!0})},b.findWhere=function(e,t){return b.where(e,t,!0)},b.max=function(e,t,r){if(!t&&b.isArray(e)&&e[0]===+e[0]&&e.length<65535)return Math.max.apply(Math,e);if(!t&&b.isEmpty(e))return-1/0;var i={computed:-1/0,value:-1/0};return w(e,function(e,n,o){var a=t?t.call(r,e,n,o):e;a>=i.computed&&(i={value:e,computed:a})}),i.value},b.min=function(e,t,r){if(!t&&b.isArray(e)&&e[0]===+e[0]&&e.length<65535)return Math.min.apply(Math,e);if(!t&&b.isEmpty(e))return 1/0;var i={computed:1/0,value:1/0};return w(e,function(e,n,o){var a=t?t.call(r,e,n,o):e;a<i.computed&&(i={value:e,computed:a})}),i.value},b.shuffle=function(e){var t,r=0,i=[];return w(e,function(e){t=b.random(r++),i[r-1]=i[t],i[t]=e}),i};var L=function(e){return b.isFunction(e)?e:function(t){return t[e]}};b.sortBy=function(e,t,r){var i=L(t);return b.pluck(b.map(e,function(e,t,n){return{value:e,index:t,criteria:i.call(r,e,t,n)}}).sort(function(e,t){var r=e.criteria,i=t.criteria;if(r!==i){if(r>i||void 0===r)return 1;if(i>r||void 0===i)return-1}return e.index<t.index?-1:1}),"value")};var S=function(e,t,r,i){var n={},o=L(t||b.identity);return w(e,function(t,a){var s=o.call(r,t,a,e);i(n,s,t)}),n};b.groupBy=function(e,t,r){return S(e,t,r,function(e,t,r){(b.has(e,t)?e[t]:e[t]=[]).push(r)})},b.countBy=function(e,t,r){return S(e,t,r,function(e,t){b.has(e,t)||(e[t]=0),e[t]++})},b.sortedIndex=function(e,t,r,i){r=null==r?b.identity:L(r);for(var n=r.call(i,t),o=0,a=e.length;a>o;){var s=o+a>>>1;r.call(i,e[s])<n?o=s+1:a=s}return o},b.toArray=function(e){return e?b.isArray(e)?s.call(e):e.length===+e.length?b.map(e,b.identity):b.values(e):[]},b.size=function(e){return null==e?0:e.length===+e.length?e.length:b.keys(e).length},b.first=b.head=b.take=function(e,t,r){return null==e?void 0:null==t||r?e[0]:s.call(e,0,t)},b.initial=function(e,t,r){return s.call(e,0,e.length-(null==t||r?1:t))},b.last=function(e,t,r){return null==e?void 0:null==t||r?e[e.length-1]:s.call(e,Math.max(e.length-t,0))},b.rest=b.tail=b.drop=function(e,t,r){return s.call(e,null==t||r?1:t)},b.compact=function(e){return b.filter(e,b.identity)};var N=function(e,t,r){return w(e,function(e){b.isArray(e)?t?a.apply(r,e):N(e,t,r):r.push(e)}),r};b.flatten=function(e,t){return N(e,t,[])},b.without=function(e){return b.difference(e,s.call(arguments,1))},b.uniq=b.unique=function(e,t,r,i){b.isFunction(t)&&(i=r,r=t,t=!1);var n=r?b.map(e,r,i):e,o=[],a=[];return w(n,function(r,i){(t?i&&a[a.length-1]===r:b.contains(a,r))||(a.push(r),o.push(e[i]))}),o},b.union=function(){return b.uniq(l.apply(i,arguments))},b.intersection=function(e){var t=s.call(arguments,1);return b.filter(b.uniq(e),function(e){return b.every(t,function(t){return b.indexOf(t,e)>=0})})},b.difference=function(e){var t=l.apply(i,s.call(arguments,1));return b.filter(e,function(e){return!b.contains(t,e)})},b.zip=function(){for(var e=s.call(arguments),t=b.max(b.pluck(e,"length")),r=new Array(t),i=0;t>i;i++)r[i]=b.pluck(e,""+i);return r},b.object=function(e,t){if(null==e)return{};for(var r={},i=0,n=e.length;n>i;i++)t?r[e[i]]=t[i]:r[e[i][0]]=e[i][1];return r},b.indexOf=function(e,t,r){if(null==e)return-1;var i=0,n=e.length;if(r){if("number"!=typeof r)return i=b.sortedIndex(e,t),e[i]===t?i:-1;i=0>r?Math.max(0,n+r):r}if(v&&e.indexOf===v)return e.indexOf(t,r);for(;n>i;i++)if(e[i]===t)return i;return-1},b.lastIndexOf=function(e,t,r){if(null==e)return-1;var i=null!=r;if(m&&e.lastIndexOf===m)return i?e.lastIndexOf(t,r):e.lastIndexOf(t);for(var n=i?r:e.length;n--;)if(e[n]===t)return n;return-1},b.range=function(e,t,r){arguments.length<=1&&(t=e||0,e=0),r=arguments[2]||1;for(var i=Math.max(Math.ceil((t-e)/r),0),n=0,o=new Array(i);i>n;)o[n++]=e,e+=r;return o},b.bind=function(e,t){if(e.bind===I&&I)return I.apply(e,s.call(arguments,1));var r=s.call(arguments,2);return function(){return e.apply(t,r.concat(s.call(arguments)))}},b.partial=function(e){var t=s.call(arguments,1);return function(){return e.apply(this,t.concat(s.call(arguments)))}},b.bindAll=function(e){var t=s.call(arguments,1);return 0===t.length&&(t=b.functions(e)),w(t,function(t){e[t]=b.bind(e[t],e)}),e},b.memoize=function(e,t){var r={};return t||(t=b.identity),function(){var i=t.apply(this,arguments);return b.has(r,i)?r[i]:r[i]=e.apply(this,arguments)}},b.delay=function(e,t){var r=s.call(arguments,2);return setTimeout(function(){return e.apply(null,r)},t)},b.defer=function(e){return b.delay.apply(b,[e,1].concat(s.call(arguments,1)))},b.throttle=function(e,t){var r,i,n,o,a=0,s=function(){a=new Date,n=null,o=e.apply(r,i)};return function(){var l=new Date,u=t-(l-a);return r=this,i=arguments,0>=u?(clearTimeout(n),n=null,a=l,o=e.apply(r,i)):n||(n=setTimeout(s,u)),o}},b.debounce=function(e,t,r){var i,n;return function(){var o=this,a=arguments,s=function(){i=null,r||(n=e.apply(o,a))},l=r&&!i;return clearTimeout(i),i=setTimeout(s,t),l&&(n=e.apply(o,a)),n}},b.once=function(e){var t,r=!1;return function(){return r?t:(r=!0,t=e.apply(this,arguments),e=null,t)}},b.wrap=function(e,t){return function(){var r=[e];return a.apply(r,arguments),t.apply(this,r)}},b.compose=function(){var e=arguments;return function(){for(var t=arguments,r=e.length-1;r>=0;r--)t=[e[r].apply(this,t)];return t[0]}},b.after=function(e,t){return 0>=e?t():function(){return--e<1?t.apply(this,arguments):void 0}},b.keys=A||function(e){if(e!==Object(e))throw new TypeError("Invalid object");var t=[];for(var r in e)b.has(e,r)&&(t[t.length]=r);return t},b.values=function(e){var t=[];for(var r in e)b.has(e,r)&&t.push(e[r]);return t},b.pairs=function(e){var t=[];for(var r in e)b.has(e,r)&&t.push([r,e[r]]);return t},b.invert=function(e){var t={};for(var r in e)b.has(e,r)&&(t[e[r]]=r);return t},b.functions=b.methods=function(e){var t=[];for(var r in e)b.isFunction(e[r])&&t.push(r);return t.sort()},b.extend=function(e){return w(s.call(arguments,1),function(t){if(t)for(var r in t)e[r]=t[r]}),e},b.pick=function(e){var t={},r=l.apply(i,s.call(arguments,1));return w(r,function(r){r in e&&(t[r]=e[r])}),t},b.omit=function(e){var t={},r=l.apply(i,s.call(arguments,1));for(var n in e)b.contains(r,n)||(t[n]=e[n]);return t},b.defaults=function(e){return w(s.call(arguments,1),function(t){if(t)for(var r in t)null==e[r]&&(e[r]=t[r])}),e},b.clone=function(e){return b.isObject(e)?b.isArray(e)?e.slice():b.extend({},e):e},b.tap=function(e,t){return t(e),e};var O=function(e,t,r,i){if(e===t)return 0!==e||1/e==1/t;if(null==e||null==t)return e===t;e instanceof b&&(e=e._wrapped),t instanceof b&&(t=t._wrapped);var n=u.call(e);if(n!=u.call(t))return!1;switch(n){case"[object String]":return e==String(t);case"[object Number]":return e!=+e?t!=+t:0==e?1/e==1/t:e==+t;case"[object Date]":case"[object Boolean]":return+e==+t;case"[object RegExp]":return e.source==t.source&&e.global==t.global&&e.multiline==t.multiline&&e.ignoreCase==t.ignoreCase}if("object"!=typeof e||"object"!=typeof t)return!1;for(var o=r.length;o--;)if(r[o]==e)return i[o]==t;r.push(e),i.push(t);var a=0,s=!0;if("[object Array]"==n){if(a=e.length,s=a==t.length)for(;a--&&(s=O(e[a],t[a],r,i)););}else{var l=e.constructor,c=t.constructor;if(l!==c&&!(b.isFunction(l)&&l instanceof l&&b.isFunction(c)&&c instanceof c))return!1;for(var d in e)if(b.has(e,d)&&(a++,!(s=b.has(t,d)&&O(e[d],t[d],r,i))))break;if(s){for(d in t)if(b.has(t,d)&&!a--)break;s=!a}}return r.pop(),i.pop(),s};b.isEqual=function(e,t){return O(e,t,[],[])},b.isEmpty=function(e){if(null==e)return!0;if(b.isArray(e)||b.isString(e))return 0===e.length;for(var t in e)if(b.has(e,t))return!1;return!0},b.isElement=function(e){return!(!e||1!==e.nodeType)},b.isArray=T||function(e){return"[object Array]"==u.call(e)},b.isObject=function(e){return e===Object(e)},w(["Arguments","Function","String","Number","Date","RegExp"],function(e){b["is"+e]=function(t){return u.call(t)=="[object "+e+"]"}}),b.isArguments(arguments)||(b.isArguments=function(e){return!(!e||!b.has(e,"callee"))}),"function"!=typeof/./&&(b.isFunction=function(e){return"function"==typeof e}),b.isFinite=function(e){return isFinite(e)&&!isNaN(parseFloat(e))},b.isNaN=function(e){return b.isNumber(e)&&e!=+e},b.isBoolean=function(e){return e===!0||e===!1||"[object Boolean]"==u.call(e)},b.isNull=function(e){return null===e},b.isUndefined=function(e){return void 0===e},b.has=function(e,t){return c.call(e,t)},b.noConflict=function(){return e._=t,this},b.identity=function(e){return e},b.times=function(e,t,r){for(var i=Array(e),n=0;e>n;n++)i[n]=t.call(r,n);return i},b.random=function(e,t){return null==t&&(t=e,e=0),e+Math.floor(Math.random()*(t-e+1))};var P={escape:{"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#x27;","/":"&#x2F;"}};P.unescape=b.invert(P.escape);var U={escape:new RegExp("["+b.keys(P.escape).join("")+"]","g"),unescape:new RegExp("("+b.keys(P.unescape).join("|")+")","g")};b.each(["escape","unescape"],function(e){b[e]=function(t){return null==t?"":(""+t).replace(U[e],function(t){return P[e][t]})}}),b.result=function(e,t){if(null==e)return null;var r=e[t];return b.isFunction(r)?r.call(e):r},b.mixin=function(e){w(b.functions(e),function(t){var r=b[t]=e[t];b.prototype[t]=function(){var e=[this._wrapped];return a.apply(e,arguments),M.call(this,r.apply(b,e))}})};var C=0;b.uniqueId=function(e){var t=++C+"";return e?e+t:t},b.templateSettings={evaluate:/<%([\s\S]+?)%>/g,interpolate:/<%=([\s\S]+?)%>/g,escape:/<%-([\s\S]+?)%>/g};var k=/(.)^/,R={"'":"'","\\":"\\","\r":"r","\n":"n","	":"t","\u2028":"u2028","\u2029":"u2029"},D=/\\|'|\r|\n|\t|\u2028|\u2029/g;b.template=function(e,t,r){var i;r=b.defaults({},r,b.templateSettings);var n=new RegExp([(r.escape||k).source,(r.interpolate||k).source,(r.evaluate||k).source].join("|")+"|$","g"),o=0,a="__p+='";e.replace(n,function(t,r,i,n,s){return a+=e.slice(o,s).replace(D,function(e){return"\\"+R[e]}),r&&(a+="'+\n((__t=("+r+"))==null?'':_.escape(__t))+\n'"),i&&(a+="'+\n((__t=("+i+"))==null?'':__t)+\n'"),n&&(a+="';\n"+n+"\n__p+='"),o=s+t.length,t}),a+="';\n",r.variable||(a="with(obj||{}){\n"+a+"}\n"),a="var __t,__p='',__j=Array.prototype.join,print=function(){__p+=__j.call(arguments,'');};\n"+a+"return __p;\n";try{i=new Function(r.variable||"obj","_",a)}catch(s){throw s.source=a,s}if(t)return i(t,b);var l=function(e){return i.call(this,e,b)};return l.source="function("+(r.variable||"obj")+"){\n"+a+"}",l},b.chain=function(e){return b(e).chain()};var M=function(e){return this._chain?b(e).chain():e};b.mixin(b),w(["pop","push","reverse","shift","sort","splice","unshift"],function(e){var t=i[e];b.prototype[e]=function(){var r=this._wrapped;return t.apply(r,arguments),"shift"!=e&&"splice"!=e||0!==r.length||delete r[0],M.call(this,r)}}),w(["concat","join","slice"],function(e){var t=i[e];b.prototype[e]=function(){return M.call(this,t.apply(this._wrapped,arguments))}}),b.extend(b.prototype,{chain:function(){return this._chain=!0,this},value:function(){return this._wrapped}})}).call(this);
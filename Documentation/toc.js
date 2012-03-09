/*!
  * jquery.toc.js - A jQuery plugin that will automaticall generate a table of contents. 
  * v0.0.1
  * https://github.com/jgallen23/toc
  * copyright JGA 2012
  * MIT License
  */
!function(a){a.fn.toc=function(b){var c=this,d=a.extend({},jQuery.fn.toc.defaults,b),e=a(d.container),f=a(d.selectors,e),g=[],h=d.prefix+"-active",i=function(b){if(d.smoothScrolling){b.preventDefault();var e=a(b.target).attr("href"),f=a(e);a(d.container).animate({scrollTop:f.offset().top},400,"swing",function(){location.hash=e})}a("li",c).removeClass(h),a(b.target).parent().addClass(h)},j,k=function(b){j&&clearTimeout(j),j=setTimeout(function(){var b=a(window).scrollTop();for(var d=0,e=g.length;d<e;d++)if(g[d]>=b){a("li",c).removeClass(h),a("li:eq("+(d-1)+")",c).addClass(h);break}},50)};return d.highlightOnScroll&&(a(window).bind("scroll",k),k()),this.each(function(){var b=a("<ul/>");f.each(function(c,e){var f=a(e);g.push(f.offset().top-d.highlightOffset);var h=a("<span/>").attr("id",d.prefix+c).insertBefore(f),j=a("<a/>").text(f.text()).attr("href","#"+d.prefix+c).bind("click",i),k=a("<li/>").addClass(d.prefix+"-"+f[0].tagName.toLowerCase()).append(j);b.append(k)});var c=a(this);c.html(b)})},jQuery.fn.toc.defaults={container:"body",selectors:"h1,h2,h3",smoothScrolling:!0,prefix:"toc",highlightOnScroll:!0,highlightOffset:100}}(jQuery)
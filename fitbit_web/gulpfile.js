var gulp = require('gulp'),
    rename = require('gulp-rename'),

    webserver = require('gulp-webserver');
var tsc = require('gulp-typescript');
const sourcemaps = require('gulp-sourcemaps');
const tsProject = tsc.createProject("tsconfig.json");
var plugins = require('gulp-load-plugins')();
var less = require('gulp-less');
var path = require('path');
var express = require('express');
var proxyMiddleware = require('http-proxy-middleware');
var open = require('open');
var connectLr = require('connect-livereload');
var targetDir = "./build";

require('events').EventEmitter.prototype._maxListeners = 1500;


var errorHandler = function(error) {

    //beep(2, 170); jarek maybe install beep?
  console.log(error);
  this.emit('end');

};

var context = '/services';
var options = {
  target: 'http://localhost:9000', // target host
  changeOrigin: true,               // needed for virtual hosted sites
  ws: true,                         // proxy websockets
  pathRewrite: {
    '^/services/' : '/api/'      // rewrite paths
  },
  proxyTable: {
    'localhost:8088' : 'http://localhost:9000'
  },
  onError : function(err, req, res ) {
    res.end('Something went wrong:' + err);
  }


};

var proxy = proxyMiddleware(context, options);

// run init tasks
gulp.task('default', ['dependencies', 'ts', 'html', 'less', 'css','images']);

// run development task
gulp.task('dev', ['watch', 'serve']);



gulp.task('less', function () {
  return gulp.src('src/less/**/*.less')
    .pipe(less({
      paths: [ path.join(__dirname, 'less', 'includes') ]
    }).on('error', errorHandler ))
    .pipe(gulp.dest('./build/css'));
});

// watch for changes and run the relevant task
gulp.task('watch', function () {
  plugins.livereload.listen();
  gulp.watch('src/**/*.ts', ['ts']);
  gulp.watch('src/**/*.html', ['html']);
  gulp.watch('src/**/*.css', ['css']);
  gulp.watch('src/**/*.less', ['less']);
  gulp.watch('src/images/*', ['images']);
  gulp.watch(['src/**/*.png','src/**/*.jpg'], ['images']);

  gulp.watch(targetDir +"/templates/**")
    .on('change', plugins.livereload.changed)
    .on('error', errorHandler);

  gulp.watch(targetDir +"/css/**")
      .on('change', plugins.livereload.changed)
      .on('error', errorHandler);

  gulp.watch(targetDir +"/images/**")
      .on('change', plugins.livereload.changed)
      .on('error', errorHandler);

  gulp.watch(targetDir +"/transpiled/**")
      .on('change', plugins.livereload.changed)
      .on('error', errorHandler);
});

// move dependencies into build dir
gulp.task('dependencies', function () {
  return gulp.src([
    'node_modules/**/*.js',
      'systemjs.config.js'
  ])
    .pipe(gulp.dest('build/lib'));
});

gulp.task("ts", () => {
  var tsResult = gulp.src("src/**/*.ts")
    .pipe(sourcemaps.init())
    .pipe(tsc(tsProject));
return tsResult.js
  .pipe(sourcemaps.write("."))
  .pipe(gulp.dest("build/transpiled"));
});

// move html
gulp.task('html', function () {
  return gulp.src('src/**/*.html')
    .pipe(gulp.dest('build'))
});

// move images
gulp.task('images', function () {
  return gulp.src(['src/**/*.png','src/**/*.jpg'])
    .pipe(gulp.dest('build'))
});

// move css
gulp.task('css', function () {
  return gulp.src('src/**/*.css')
    .pipe(gulp.dest('build'))
});


gulp.task('serve', function() {
  express()
    .use( connectLr())
    .use(proxy)
    .use(express.static(targetDir))
    .listen(8088);
  open('http://localhost:' + 8088 + '/');
});

<script setup>

</script>


<template>

  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">

      <div style="flex: 1; background-color: black">
        <el-image style="width: 100%; height: 100%" fit="cover"
                  src="./src/assets/img.png">

        </el-image>

      </div>

      <div class="welcome-title">
          <div style="font-size: 20px; font-weight: bold">
              尽量不要，倘若没有必要理由，更是绝对不能，对任何人说出或写下“我太忙”这几个字；也不要用类似借口，推说“周围压力太大”，时常逃避我们对同辈和同龄人应尽的义务。</div>

      </div>

      <div class="right-card">
          <router-view/>
      </div>

  </div>

</template>


<style scoped>

.welcome-title{
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: orangered;
    text-shadow: 0 0 10px black;
}

.right-card{
    width: 400px;
    z-index: 1;
    background-color: white;
}

</style>
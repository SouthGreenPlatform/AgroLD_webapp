<script setup lang="ts">

</script>
<template>
    <div class="container-fluid arian-thread">
        <div class="row">
            <div class="info_title" style="margin-left: 11rem">
                <div class="container pos-l">
                    <slot name="baseText"></slot> > <span class="active-p">
                        <slot name="nextText"></slot>
                    </span>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped>
.arian-thread {
    height: 80px;
    padding: 10px;
    padding-left: 0px;
    background: #f3f3f3;
    font-weight: bold;
    font-size: 25px;
    color: #b3b3b3;
    z-index: 500;
    position: fixed;
    width: 100%;
    left: 0;
    top: 4rem;
    border-top: 1px solid greenyellow;
    box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.2), 0 6px 6px 0 rgba(0, 0, 0, 0.19);
}

.info_title {
    font-weight: bold;
    font-size: 5rem;
    color: #b3b3b3;
    padding-left: 0px;
}

.active-p {
    color: #858d85;
}

/* Media Queries */

@media (max-width: 600px) {
    .arian-thread {
        height: 40px;
        font-size: 20px;
        padding: 5px;
        padding-left: 0px;

    }

    .info_title {
        font-size: 20px;
    }
}

@media (min-width: 601px) and (max-width: 1024px) {
    .arian-thread {
        height: 70px;
        font-size: 22px;
        padding: 8px;
        padding-left: 0px;
    }

    .info_title {
        font-size: 22px;
    }
}

@media (min-width: 1024px) {
    .arian-thread {
        height: 60px;
        font-size: 22px;
        padding: 8px;
        padding-top: 14px;
        padding-left: 0px;
        top: 4.5rem;
    }

    .info_title {
        font-size: 22px;
    }
}

@media (max-width: 1024px) {
    .arian-thread {
        height: 50px;
        font-size: 22px;
        padding: 8px;
        padding-left: 0px;
        top: 4.5rem;
    }

    .info_title {
        font-size: 22px;
    }
}
</style>
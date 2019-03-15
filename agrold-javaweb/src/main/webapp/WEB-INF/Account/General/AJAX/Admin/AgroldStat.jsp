<%-- 
    Document   : AgroldStat
    Created on : 30 août 2017, 14:45:49
    Author     : Jc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>



<div class="o-frame">
    <h1 class="o-frame-title">Data manager</h1>
    <hr>
    <h2>Choose an interval to remove history from requested tools</h2>
    <hr>
    <h3>Start date</h3>
    <div class="o-no">
        <div class="col" style="height:130px;">
            <div class="form-group">
                <div class="input-group date" id="getStartDate">
                    <span class="input-group-addon">
                        Day
                    </span>
                    <select class="form-control day">
                        <option value="01">1</option>
                        <option value="02">2</option>
                        <option value="03">3</option>
                        <option value="04">4</option>
                        <option value="05">5</option>
                        <option value="06">6</option>
                        <option value="07">7</option>
                        <option value="08">8</option>
                        <option value="09">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                        <option value="13">13</option>
                        <option value="14">14</option>
                        <option value="15">15</option>
                        <option value="16">16</option>
                        <option value="17">17</option>
                        <option value="18">18</option>
                        <option value="19">19</option>
                        <option value="20">20</option>
                        <option value="21">21</option>
                        <option value="22">22</option>
                        <option value="23">23</option>
                        <option value="24">24</option>
                        <option value="25">25</option>
                        <option value="26">26</option>
                        <option value="27">27</option>
                        <option value="28">28</option>
                        <option value="29">29</option>
                        <option value="30">30</option>
                        <option value="31">31</option>
                    </select>
                    <span class="input-group-addon">
                        Month
                    </span>
                    <select class="form-control month">
                        <option value="01">1</option>
                        <option value="02">2</option>
                        <option value="03">3</option>
                        <option value="04">4</option>
                        <option value="05">5</option>
                        <option value="06">6</option>
                        <option value="07">7</option>
                        <option value="08">8</option>
                        <option value="09">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                    </select>
                    <span class="input-group-addon">
                        Year
                    </span>
                    <select class="form-control year">
                        <option value="2017">2017</option>
                        <option value="2018">2018</option>
                        <option value="2019">2019</option>
                        <option value="2020">2020</option>
                        <option value="2021">2021</option>
                        <option value="2022">2022</option>
                        <option value="2023">2023</option>
                    </select>
                </div>
            </div>
        </div>
        <h3>End date</h3>
        <div class="col" style="height:130px;">
            <div class="form-group">
                <div class="input-group date" id="getEndDate">
                    <span class="input-group-addon">
                        Day
                    </span>
                    <select class="form-control day">
                        <option value="01">1</option>
                        <option value="02">2</option>
                        <option value="03">3</option>
                        <option value="04">4</option>
                        <option value="05">5</option>
                        <option value="06">6</option>
                        <option value="07">7</option>
                        <option value="08">8</option>
                        <option value="09">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                        <option value="13">13</option>
                        <option value="14">14</option>
                        <option value="15">15</option>
                        <option value="16">16</option>
                        <option value="17">17</option>
                        <option value="18">18</option>
                        <option value="19">19</option>
                        <option value="20">20</option>
                        <option value="21">21</option>
                        <option value="22">22</option>
                        <option value="23">23</option>
                        <option value="24">24</option>
                        <option value="25">25</option>
                        <option value="26">26</option>
                        <option value="27">27</option>
                        <option value="28">28</option>
                        <option value="29">29</option>
                        <option value="30">30</option>
                        <option value="31">31</option>
                    </select>
                    <span class="input-group-addon">
                        Month
                    </span>
                    <select class="form-control month">
                        <option value="01">1</option>
                        <option value="02">2</option>
                        <option value="03">3</option>
                        <option value="04">4</option>
                        <option value="05">5</option>
                        <option value="06">6</option>
                        <option value="07">7</option>
                        <option value="08">8</option>
                        <option value="09">9</option>
                        <option value="10">10</option>
                        <option value="11">11</option>
                        <option value="12">12</option>
                    </select>
                    <span class="input-group-addon">
                        Year
                    </span>
                    <select class="form-control year">
                        <option value="2017">2017</option>
                        <option value="2018">2018</option>
                        <option value="2019">2019</option>
                        <option value="2020">2020</option>
                        <option value="2021">2021</option>
                        <option value="2022">2022</option>
                        <option value="2023">2023</option>
                    </select>
                </div>
            </div>
            <div class="row">

                <label class="radio-inline"><input id="chk-un" type="radio" name="optradio" checked>&nbsp;Anonymous Only&nbsp;</label>
                <label class="radio-inline"><input id="chk-de" type="radio" name="optradio">&nbsp;Users Only&nbsp;</label>
                <label class="radio-inline"><input id="chk-oi" type="radio" name="optradio">&nbsp;Both&nbsp;</label> 
            </div>
            <div class="row">
                <a href="javascript:void(0)" class="o-mi btn btn-outline-warning" id="deleteUserHistory" value="1-1-2017:1-1-2017">Delete history</a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

</script>

package cj.geochat.imc.common;

import org.junit.jupiter.api.Test;

public class TestFrame {
    @Test
    public void testFrame1() {
        ImcFrame frame = new ImcFrame("get /test?a=1&b=2 imc/1.0", "tester@ch-29920x.chat:*");
        frame.header("Test", "xxxx");
        printFrame(frame);
    }

    @Test
    public void testFrame2() {
        ImcFrame frame = new ImcFrame("post /test/folder1/folder2/23.html?a=1&b=2 imc/1.0", "Jz-39923.session@ch-92888s.group:cj,wel~tom,cat");
        frame.header("Test", "xxxx");
        printFrame(frame);
    }

    @Test
    public void testFrame3() {
        ImcFrame frame = new ImcFrame("post /test/folder1/folder2/23.html?a=1&b=2 imc/1.0", "Jz-39923.session@ch-92888s.group:cj,wel~");
        frame.header("Test", "xxxx");
        frame.body().append("""
                在Html5中<header>叫作页眉，它在Html5基础结构中的<body>元素中，
                <head>和<header>最大的区别就是，<header>元素内容会显示在页面，
                <head>元素内容不显示在页面中，<header>元素位置如图2在<body>元素内所以会在页显示，
                <head>元素在<body>元素外的顶部所以不在页面显示。显示在页面中的内容都放在<body>元素中。
                                
                作者：巨汉子
                链接：https://www.jianshu.com/p/3550f3d35199
                来源：简书
                著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。""");
        printFrame(frame);
    }

    @Test
    public void testFrameConvert1() {
        ImcFrame frame = new ImcFrame("post /test/folder1/folder2/23.html?a=1&b=2 imc/1.0", "Jz-39923.session@ch-92888s.group:cj,wel~li,wj");
        frame.header("Test", "xxxx");
        frame.body().append("""
                在Html5中<header>叫作页眉，它在Html5基础结构中的<body>元素中，
                <head>和<header>最大的区别就是，<header>元素内容会显示在页面，
                <head>元素内容不显示在页面中，<header>元素位置如图2在<body>元素内所以会在页显示，
                <head>元素在<body>元素外的顶部所以不在页面显示。显示在页面中的内容都放在<body>元素中。
                                
                作者：巨汉子
                链接：https://www.jianshu.com/p/3550f3d35199
                来源：简书
                著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。""");
        String raw = frame.toText();
        frame = ImcFrame.fromText(raw);
        printFrame(frame);
    }

    @Test
    public void testFrameConvert2() {
        ImcFrame frame = new ImcFrame("post /test/folder1/folder2/23.html?a=1&b=2 imc/1.0", "Jz-39923.session@ch-92888s.group:cj,wel~li,wj");
        frame.header("Test", "xxxx");
        frame.header("Test2", "xxxx2");
        String raw = frame.toText();
        frame = ImcFrame.fromText(raw);
        printFrame(frame);
    }

    @Test
    public void testFrameConvert3() {
        ImcFrame frame = new ImcFrame("post /test/folder1/folder2/23.html?a=1&b=2 imc/1.0", "Jz-39923.session@ch-92888s.group:cj,wel~li,wj");
        frame.header("Test", "xxxx");
        frame.body().append("""
                在Html5中<header>叫作页眉，它在Html5基础结构中的<body>元素中，
                <head>和<header>最大的区别就是，<header>元素内容会显示在页面，
                <head>元素内容不显示在页面中，<header>元素位置如图2在<body>元素内所以会在页显示，
                <head>元素在<body>元素外的顶部所以不在页面显示。显示在页面中的内容都放在<body>元素中。
                                
                作者：巨汉子
                链接：https://www.jianshu.com/p/3550f3d35199
                来源：简书
                著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。""");
        byte[] raw = frame.toBytes();
        frame = ImcFrame.fromBytes(raw);
        printFrame(frame);
    }

    @Test
    public void testFrameConvert4() {
        ImcFrame frame = new ImcFrame("post /test/folder1/folder2/23.html?a=1&b=2 imc/1.0", "Jz-39923.session@ch-92888s.group:cj,wel~li,wj");
        frame.header("Test", "xxxx");
        frame.body().append("""
                在Html5中<header>叫作页眉，它在Html5基础结构中的<body>元素中，
                <head>和<header>最大的区别就是，<header>元素内容会显示在页面，
                <head>元素内容不显示在页面中，<header>元素位置如图2在<body>元素内所以会在页显示，
                <head>元素在<body>元素外的顶部所以不在页面显示。显示在页面中的内容都放在<body>元素中。
                                
                作者：巨汉子
                链接：https://www.jianshu.com/p/3550f3d35199
                来源：简书
                著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。""");
        String raw = frame.toJson();
        frame = ImcFrame.fromJson(raw);
        printFrame(frame);
    }

    @Test
    public void testFrameError1() {
        ImcFrame frame = new ImcFrame("post   /test/folder1/folder2/23.html?a=1&b=2 imc/1.0", "Jz-39923");
        frame.header("Test", "xxxx");
        printFrame(frame);
    }

    private void printFrame(ImcFrame frame) {
        System.out.println(frame);
        System.out.println("--------------------------------------------");
        System.out.println(String.format("\tschema: %s", frame.schema()));
        System.out.println(String.format("\tmethod: %s", frame.method()));
        System.out.println(String.format("\tversion: %s", frame.version()));
        System.out.println(String.format("\tchannel: %s", frame.channel()));
        System.out.println(String.format("\tsender: %s", frame.sender()));
        System.out.println(String.format("\tport: %s", frame.port()));
        System.out.println(String.format("\tpath: %s", frame.path()));
        System.out.println(String.format("\tqueryString: %s", frame.queryString()));
        System.out.println(String.format("\trecipients: %s", frame.recipients()));
        System.out.println(String.format("\trejects: %s", frame.rejects()));
        System.out.println("\trecipient list---------------begin");
        for (String recipient : frame.recipientsToList()) {
            System.out.println(String.format("\t\t%s", recipient));
        }
        System.out.println("\tend");
        System.out.println("\treject list------------------begin");
        for (String reject : frame.rejectsToList()) {
            System.out.println(String.format("\t\t%s", reject));
        }
        System.out.println("\tend");
        System.out.println("\thead list--------------------begin");
        for (String key : frame.enumHeader()) {
            String value = frame.header(key);
            System.out.println(String.format("\t\t//contains header %s: %s", key, frame.containsHeader(key)));
            System.out.println(String.format("\t\t%s: %s", key, value));
        }
        System.out.println("\tend");
        System.out.println("\tparameter list---------------begin");
        for (String key : frame.enumParameter()) {
            String value = frame.parameter(key);
            System.out.println(String.format("\t\t//contains parameter %s: %s", key, frame.containsParameter(key)));
            System.out.println(String.format("\t\t%s: %s", key, value));
        }
        System.out.println("\tend");
        System.out.println("body-----begin");
        System.out.println(frame.body());
        System.out.println("--end");
    }
}

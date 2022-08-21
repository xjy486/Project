package com.nuist;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/*ATM系统入口类*/
public class ATMSystem {
    public static void main(String[] args) {
        //1.定义账户类
        //2.定义集合容器，存储账户对象
        ArrayList<Account> accounts = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        //3.展示系统的首页
        while (true) {
            System.out.println("====================米酒ATM系统===================");
            System.out.println("====================米酒ATM系统===================");
            System.out.println("1.账户登录");
            System.out.println("2.账户开户");

            System.out.println("请您选择操作: ");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    //账户登录操作
                    login(accounts, sc);
                    break;
                case 2:
                    //账户开户操作
                    register(accounts, sc);
                    break;
                default:
                    System.out.println("您输入的操作不存在");
            }
        }
    }

    /**
     * 用户登录功能
     *
     * @param accounts 全部的账户集合
     * @param sc       扫描器
     */
    private static void login(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("====================系统登录操作===================");
        //1.判断账户集合中是否存在账户，如果不存在账户，则登录功能不能进行
        if (accounts.size() == 0) {
            System.out.println("对不起，当前系统中无任何账户，请先开户，再进行登录");
            return;
        }
        //2.正式进入登录操作
        while (true) {
            System.out.println("请输入登录卡号：");
            String cardId = sc.next();
            //3.判断卡号是否存在
            Account acc = getAccountByCardId(cardId, accounts);
            if (acc != null) {
                //卡号存在
                //4.请用户输入密码
                while (true) {
                    System.out.println("请您输入密码：");
                    String passWord = sc.next();
                    if (passWord.equals(acc.getPassWord())) {
                        //登录成功了
                        System.out.println("欢迎，" + acc.getUserName() + "先生/女士进入系统，您的卡号是" + acc.getCardId());
                        //....查询 转账 取款 ....
                        //展示登录后的操作页
                        showUserCommand(sc, acc, accounts);
                        return;
                    } else {
                        System.out.println("对不起，密码错误，请重新输入");
                    }
                }
            } else {
                System.out.println("对不起，系统中不存在该账户卡号");
            }
        }
    }

    /**
     * 展示登录后的操作页
     */
    private static void showUserCommand(Scanner sc, Account acc, ArrayList<Account> accounts) {
        while (true) {
            System.out.println("====================用户操作页===================");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.修改密码");
            System.out.println("6.退出登录");
            System.out.println("7.注销账户");
            System.out.println("请选择： ");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    //1.查询账户
                    showAccount(acc);
                    break;
                case 2:
                    //2.存款
                    depositMoeny(acc, sc);
                    break;
                case 3:
                    //3.取款
                    drawMoney(acc, sc);
                    break;
                case 4:
                    //4.转账
                    transferMoney(acc, sc, accounts);
                    break;
                case 5:
                    //5.修改密码
                    updatePassWord(acc,sc);
                    return;
                case 6:
                    //6.退出登录
                    System.out.println("退出成功，欢迎下次光临");
                    return;
                case 7:
                    //7.注销账户
                    //从当前账户列表中删除账户对象
                    System.out.println("听说您打算销户？ y/n");
                    String rs=sc.next();
                    switch (rs){
                        case "y":
                            //销户
                            accounts.remove(acc);
                            System.out.println("您的账户销户完成");
                            break;
                        default:
                            System.out.println("当前账户继续保留");
                            break;
                    }
                    break;
                default:
                    System.out.println("您输入的操作不存在");
                    break;
            }
        }
    }

    /**
     * 修改账户密码操作
     * @param acc
     * @param sc
     */
    private static void updatePassWord(Account acc, Scanner sc) {
        System.out.println("====================修改密码===================");
        while (true) {
            System.out.println("请您输入当前密码");
            String passWord = sc.next();
            if(passWord.equals(acc.getPassWord())){
                //密码正确
                //输入新密码
                System.out.println("请输入新密码");
                String newPassWord=sc.next();
                acc.setPassWord(newPassWord);

                System.out.println("密码修改成功");
                return;
            }else{
                System.out.println("您输入的密码不正确，请重新输入");
            }
        }
    }

    /**
     * 向对方账户进行转账
     *
     * @param acc      自己的账户对象
     * @param sc       扫描器
     * @param accounts 账户列表
     */
    private static void transferMoney(Account acc, Scanner sc, ArrayList<Account> accounts) {
        System.out.println("====================用户转账===================");
        //1.是否足够2个账户
        if (accounts.size() < 2) {
            System.out.println("当前系统中，不足2个账户，不能进行转账，请去开户吧");
            return;
        }
        //2.判断自身账户是否有钱
        if (acc.getMoney() == 0) {
            System.out.println("对不起，您自己都没钱，还转啥呀");
            return;
        }
        while (true) {
            //3.真正开始转账
            System.out.println("请输入对方的卡号");
            String cardId = sc.next();
            if (cardId.equals(acc.getCardId())) {
                System.out.println("不可以输入自己的卡号");
                continue;//结束循环，进入下一次
            }
            //判断卡号是否存在，根据这个卡号去查询对方账户对象
            Account account = getAccountByCardId(cardId, accounts);
            if (account == null) {
                System.out.println("对不起，您输入的账户不存在");
            } else {
                //这个账户存在，继续认证姓氏
                String userName = account.getUserName();
                String tip = "*" + userName.substring(1);
                System.out.println("请输入[" + tip + "]的姓氏");
                String preName = sc.next();
                //认证姓氏是否输入正确
                //startswith检测字符串是否以指定的前缀开始
                if(userName.startsWith(preName)){
                    //认证成功，真正开始转账了
                    while (true) {
                        System.out.println("请您输入转账金额：");
                        double money=sc.nextDouble();
                        if (money > acc.getQuotaMoney()) {
                            System.out.println("您当前转账金额超过了单次取款最大限额，您当前单次取款金额为" + acc.getQuotaMoney() + "请重新输入转账金额");
                        } else if (money > acc.getMoney()) {
                            System.out.println("您当前转账金额超过账户余额，请重新输入转账金额");
                        } else {
                            acc.setMoney(acc.getMoney() - money);
                            account.setMoney(account.getMoney()+money);
                            System.out.println("转账成功，当前账户信息如下：");
                            showAccount(acc);
                            return;
                        }
                    }
                }else{
                    System.out.println("对不起您输入的信息有误");
                }

            }
        }
    }

    /**
     * 在当前账户中取钱
     *
     * @param acc
     * @param sc
     */
    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("====================用户取款===================");
        if (acc.getMoney() < 100) {
            System.out.println("您当前的账户余额不足100元，请存满100元后再来取款");
            return;
        } else {
            while (true) {
                System.out.println("请输入取款金额：");
                double money = sc.nextDouble();
                if (money > acc.getQuotaMoney()) {
                    System.out.println("您当前取款超过了单次取款最大限额，您当前单次取款金额为" + acc.getQuotaMoney() + "请重新输入取款金额");
                } else if (money > acc.getMoney()) {
                    System.out.println("您当前取款超过账户余额，请重新输入取款金额");
                } else {
                    acc.setMoney(acc.getMoney() - money);
                    System.out.println("取款成功，当前账户信息如下：");
                    showAccount(acc);
                    return;
                }
            }

        }
    }


    /**
     * 在当前账户中存钱
     *
     * @param acc
     */
    private static void depositMoeny(Account acc, Scanner sc) {
        System.out.println("====================用户存款===================");
        System.out.println("请输入存款金额：");
        double money = sc.nextDouble();
        //更新账户余额，原来的钱加现在的钱
        acc.setMoney(acc.getMoney() + money);
        System.out.println("存钱成功，当前账户信息如下：");
        showAccount(acc);
        return;
    }

    /**
     * 展示当前登录账户
     */
    private static void showAccount(Account acc) {
        System.out.println("====================账户信息===================");
        System.out.println("卡号" + acc.getCardId());
        System.out.println("户主" + acc.getUserName());
        System.out.println("余额" + acc.getMoney());
        System.out.println("单次取款金额" + acc.getQuotaMoney());
    }

    /**
     * 用户开户功能的实现
     *
     * @param accounts 全部的账户集合
     * @param sc       扫描器
     */
    private static void register(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("====================系统开户操作===================");
        //1.创建一个账户对象，用于后期封装账户信息
        Account account = new Account();
        //2.录入当前的账户信息，注入到账户对象中去
        System.out.println("请输入账户用户名：");
        String username = sc.next();
        account.setUserName(username);

        while (true) {
            System.out.println("请输入账户密码：");
            String passWord = sc.next();
            System.out.println("请输入确认账户密码：");
            String okPassWord = sc.next();
            if (passWord.equals(okPassWord)) {
                account.setPassWord(okPassWord);
                break;
            } else {
                System.out.println("对不起，您输入的两次密码不一致，请重新输入");
            }
        }
        System.out.println("请输入账户单次限额：");
        double quotaMoney = sc.nextDouble();
        account.setQuotaMoney(quotaMoney);

        //为账户随机生成一个8位数字卡号且与其他卡号不重复
        String cardId = getRandomCardId(accounts);
        account.setCardId(cardId);

        //3.把账户对象添加到某个账户集合中去
        accounts.add(account);
        System.out.println("恭喜您，" + username + "先生/女士，您开户成功，您的卡号是：" + cardId + "，请妥善保管。");
    }

    /**
     * 为账户生成与其他卡号不同的卡号
     */
    private static String getRandomCardId(ArrayList<Account> accounts) {
        Random r = new Random();
        while (true) {
            //1.生成8位数字
            String cardId = "";

            for (int i = 0; i < 8; i++) {
                cardId += r.nextInt(10);
            }
            //2.判断这8位数字是否与之前的重复
            Account acc = getAccountByCardId(cardId, accounts);
            if (acc == null) {
                //说明cardId没有重复
                return cardId;
            }
        }
    }

    /**
     * 根据卡号查询出一个账户对象
     *
     * @param cardId   卡号
     * @param accounts 账户列表
     * @return
     */
    private static Account getAccountByCardId(String cardId, ArrayList<Account> accounts) {
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardId().equals(cardId)) {
                return acc;
            }
        }
        return null;
    }
}

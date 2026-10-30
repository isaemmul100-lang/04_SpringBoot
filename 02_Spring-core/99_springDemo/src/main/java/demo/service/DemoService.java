package demo.service;

import demo.repository.DemoRepository;
import demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    private final MemberRepository memberRepository;
    private final DemoRepository demoRepository;

    public DemoService(MemberRepository memberRepository, DemoRepository demoRepository) {
        this.memberRepository = memberRepository;
        this.demoRepository = demoRepository;
    }
}

import React from 'react';

type Props = {match:any, location:any, history:any};
type State = {}
class Detail extends React.Component<Props, State> {
    render(){
        return (
            <div>
                Detail {this.props.match.params.id}!
            </div>
        );
    }
}

export default Detail;